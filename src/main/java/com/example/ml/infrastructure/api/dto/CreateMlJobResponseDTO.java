package com.example.ml.infrastructure.api.dto;

import com.example.ml.core.model.MlJobStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "ML job creation response")
public record CreateMlJobResponseDTO(
        @Schema(description = "Created job identifier",
                example = "eca39cf2-c6aa-4321-94fa-4f97835a4daf")
        UUID jobId,

        @Schema(description = "Associated record identifier",
                example = "ac31fb6b-f486-4a51-95f7-45a6c5e02337")
        UUID recordId,

        @Schema(description = "Initial job status",
                example = "QUEUED")
        MlJobStatus status
) {
}
