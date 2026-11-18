package com.example.ml.infrastructure.client.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;

@Configuration
@ConditionalOnProperty(prefix = "ml.processing", name = "mock-enabled", havingValue = "false", matchIfMissing = true)
@EnableConfigurationProperties(QwenProperties.class)
public class QwenClientConfig {

    @Bean
    public RestClient qwenRestClient(QwenProperties properties) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(properties.connectTimeout())
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);

        requestFactory.setReadTimeout(properties.readTimeout());
        RestClient.Builder builder = RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl(properties.url());

        if (properties.apiKey() != null && !properties.apiKey().isBlank()) {
            builder.defaultHeaders(headers -> headers.setBearerAuth(properties.apiKey()));
        }

        return builder.build();
    }
}
