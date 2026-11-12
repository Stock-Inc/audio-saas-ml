package com.example.ml.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "ML job processing options",
        example = """
        {
          "diarization": true,
          "summary": false,
          "tasks": true
        }
        """)
public record MlJobOptions(
        @Schema(description = "Enable speaker diarization")
        boolean diarization,

        @Schema(description = "Generate summary")
        boolean summary,

        @Schema(description = "Extract tasks")
        boolean tasks
) {
}
