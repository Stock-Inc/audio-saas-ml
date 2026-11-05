package com.example.ml.infrastructure.client.dto;

public record WhisperSegmentDto(
        String speaker,
        Double start,
        Double end,
        String text
) {
}
