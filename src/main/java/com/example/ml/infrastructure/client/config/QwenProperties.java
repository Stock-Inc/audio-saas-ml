package com.example.ml.infrastructure.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.time.Duration;

@ConfigurationProperties(prefix = "ml.qwen")
public record QwenProperties(
        URI url,
        String model,
        Duration readTimeout,
        Duration connectTimeout,
        String apiKey,
        String systemPrompt
) {
}
