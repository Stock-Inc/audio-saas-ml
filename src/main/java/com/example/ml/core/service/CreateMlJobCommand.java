package com.example.ml.core.service;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Builder
public record CreateMlJobCommand(
        UUID requestId,
        UUID recordId,
        // TODO: change MultipartFile
        MultipartFile file,
        boolean diarization,
        boolean summary,
        boolean tasks
) {
}
