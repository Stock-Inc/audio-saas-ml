package com.example.ml.infrastructure.client;

import com.example.ml.core.model.MlResult;
import com.example.ml.core.port.outgoing.SttClientPort;
import com.example.ml.infrastructure.client.dto.WhisperResponse;
import com.example.ml.infrastructure.client.mapper.MlResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@ConditionalOnProperty(prefix = "ml.processing", name = "mock-enabled", havingValue = "false", matchIfMissing = true)
@RequiredArgsConstructor
public class WhisperClient implements SttClientPort {
    private final RestClient whisperRestClient;
    private final MlResultMapper mlResultMapper;

    @Override
    public MlResult.Transcript transcribe(String audioFilePath, boolean diarization) {
        FileSystemResource audioFile = new FileSystemResource(audioFilePath);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", audioFile);

        WhisperResponse response = whisperRestClient.post()
                .uri("/transcribe")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(WhisperResponse.class);

        List<MlResult.Segment> domainSegments = mlResultMapper.toDomainSegments(response.segments());
        return new MlResult.Transcript(domainSegments);
    }
}
