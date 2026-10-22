package com.example.ml.infrastructure.client;

import com.example.ml.core.port.outgoing.WebhookPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Component
public class RestTemplateWebhookAdapter implements WebhookPort {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendCallback(String callbackUrl, UUID jobId, UUID recordId, String status) {
        if (callbackUrl == null || callbackUrl.isBlank()) {
            return;
        }

        // TODO: callback DTO
        Map<String, Object> payload = Map.of(
                "jobId", jobId,
                "recordId", recordId,
                "status", status,
                "progress", 100
        );

        restTemplate.postForEntity(callbackUrl, payload, Void.class);
    }
}