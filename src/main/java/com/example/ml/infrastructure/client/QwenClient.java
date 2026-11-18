package com.example.ml.infrastructure.client;

import com.example.ml.core.model.MlResult;
import com.example.ml.core.port.outgoing.AnalysisClientPort;
import com.example.ml.infrastructure.client.dto.QwenChatRequest;
import com.example.ml.infrastructure.client.dto.QwenChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@ConditionalOnProperty(prefix = "ml.processing", name = "mock-enabled", havingValue = "false", matchIfMissing = true)
@RequiredArgsConstructor
public class QwenClient implements AnalysisClientPort {

    private final RestClient qwenRestClient;
    private final QwenAnalysisCodec codec;

    @Override
    public MlResult.Analysis analyze(MlResult.Transcript transcript, boolean summary, boolean tasks) {
        if (!summary && !tasks) {
            return null;
        }

        QwenChatRequest request = codec.createRequest(transcript, summary, tasks);
        QwenChatResponse response = qwenRestClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(QwenChatResponse.class);

        return codec.toAnalysis(response, summary, tasks);
    }
}
