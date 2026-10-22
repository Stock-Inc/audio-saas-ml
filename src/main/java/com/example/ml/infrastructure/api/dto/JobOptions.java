package com.example.ml.infrastructure.api.dto;

public record JobOptions(
        boolean diarization,
        boolean summary,
        boolean tasks
) {
}
