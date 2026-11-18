package com.example.ml.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record QwenChatRequest(
        String model,
        boolean stream,
        @JsonProperty("response_format") ResponseFormat responseFormat,
        List<Message> messages
) {

    public record ResponseFormat(String type) {
    }

    public record Message(String role, String content) {
    }
}
