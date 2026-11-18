package com.example.ml.infrastructure.client;

import com.example.ml.core.model.MlResult;
import com.example.ml.infrastructure.client.config.QwenProperties;
import com.example.ml.infrastructure.client.dto.QwenAnalysisResponse;
import com.example.ml.infrastructure.client.dto.QwenChatRequest;
import com.example.ml.infrastructure.client.dto.QwenChatResponse;
import com.example.ml.infrastructure.client.mapper.QwenAnalysisMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.CoercionAction;
import tools.jackson.databind.cfg.CoercionInputShape;
import tools.jackson.databind.type.LogicalType;

import java.util.List;

@Component
@ConditionalOnProperty(prefix = "ml.processing", name = "mock-enabled", havingValue = "false", matchIfMissing = true)
@RequiredArgsConstructor
public class QwenAnalysisCodec {

    private final QwenProperties properties;
    private final ObjectMapper objectMapper;
    private final QwenAnalysisMapper mapper;

    public MlResult.Analysis toAnalysis(QwenChatResponse response, boolean summary, boolean tasks) {
        QwenAnalysisResponse result = parseResponse(response);
        if (summary && (result.summary() == null || result.topics() == null
                || result.keyPoints() == null || result.decisions() == null
                || result.topics().contains(null) || result.keyPoints().contains(null)
                || result.decisions().contains(null))) {
            throw invalidResponse("missing summary fields");
        }
        if (tasks && (result.tasks() == null || result.tasks().stream()
                .anyMatch(task -> task == null || task.text() == null))) {
            throw invalidResponse("missing task fields");
        }

        return mapper.toDomainAnalysis(result, summary, tasks);
    }

    public QwenChatRequest createRequest(MlResult.Transcript transcript, boolean summary, boolean tasks) {
        String summaryInstruction = summary
                ? "Include summary, topics, keyPoints and decisions. "
                : "Omit summary, topics, keyPoints and decisions. ";
        String tasksInstruction = tasks ? "Include tasks." : "Omit tasks.";
        String prompt = properties.systemPrompt().stripTrailing() + "\n" + summaryInstruction + tasksInstruction;

        return new QwenChatRequest(
                properties.model(),
                false,
                new QwenChatRequest.ResponseFormat("json_object"),
                List.of(
                        new QwenChatRequest.Message("system", prompt),
                        new QwenChatRequest.Message("user", objectMapper.writeValueAsString(transcript))
                )
        );
    }

    private QwenAnalysisResponse parseResponse(QwenChatResponse response) {
        if (response == null || response.choices() == null || response.choices().isEmpty()
                || response.choices().getFirst() == null) {
            throw invalidResponse("missing choices");
        }
        QwenChatResponse.Choice choice = response.choices().getFirst();
        if (choice.finishReason() != null && !"stop".equals(choice.finishReason())) {
            throw invalidResponse("completion did not finish normally");
        }
        if (choice.message() == null || choice.message().content() == null) {
            throw invalidResponse("missing message content");
        }

        QwenAnalysisResponse analysis = objectMapper.rebuild()
                .disable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
                .withCoercionConfig(LogicalType.Textual, config -> config
                        .setCoercion(CoercionInputShape.Integer, CoercionAction.Fail)
                        .setCoercion(CoercionInputShape.Float, CoercionAction.Fail)
                        .setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail))
                .build()
                .readerFor(QwenAnalysisResponse.class)
                .with(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
                .readValue(choice.message().content());
        if (analysis == null) {
            throw invalidResponse("empty analysis");
        }
        return analysis;
    }

    private static RuntimeException invalidResponse(String reason) {
        // TODO: exceptions
        return new RuntimeException("Invalid Qwen response: " + reason);
    }
}
