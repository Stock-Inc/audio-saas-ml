package com.example.ml.infrastructure.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.time.Duration;

@ConfigurationProperties(prefix = "ml.whisper")
public record WhisperProperties(
        URI url,
        Duration readTimeout,
        Duration connectTimeout

) {
}
