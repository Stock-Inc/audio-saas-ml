package com.example.ml.infrastructure.api.dto;

import com.example.ml.core.model.MlJobStatus;
import com.example.ml.core.model.MlResult;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Completed ML job result")
public record MlJobResultResponseDTO(

        @Schema(description = "Unique ML job identifier",
                example = "eca39cf2-c6aa-4321-94fa-4f97835a4daf")
        UUID jobId,

        @Schema(description = "Associated record identifier",
                example = "ac31fb6b-f486-4a51-95f7-45a6c5e02337")
        UUID recordId,

        @Schema(description = "Current ML job status",
                example = "COMPLETED")
        MlJobStatus status,

        @Schema(description = "Audio transcription with speaker information")
        MlResult.Transcript transcript,

        @Schema(description = "ML-generated analysis results")
        MlResult.Analysis analysis
) {}
