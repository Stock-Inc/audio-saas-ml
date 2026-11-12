package com.example.ml.infrastructure.api.controller;

import com.example.ml.core.model.MlJobStatus;
import com.example.ml.core.service.CreateMlJobCommand;
import com.example.ml.core.service.MlJobService;
import com.example.ml.infrastructure.api.dto.CreateMlJobResponseDTO;
import com.example.ml.infrastructure.api.dto.MlJobOptions;
import com.example.ml.infrastructure.api.dto.MlJobResultResponseDTO;
import com.example.ml.infrastructure.api.dto.MlJobStatusResponseDTO;
import com.example.ml.infrastructure.api.mapper.MlJobApiMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ml/jobs")
@RequiredArgsConstructor
@Tag(name = "ML jobs")
public class MlJobController {
    private final MlJobService mlJobService;
    private final MlJobApiMapper mapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Create ML job",
            description = "Creates a new ML processing job",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            encoding = @Encoding(
                                    name = "options",
                                    contentType = MediaType.APPLICATION_JSON_VALUE
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "202",
                    description = "ML job accepted for processing",
                    content = @Content(
                            schema = @Schema(implementation = CreateMlJobResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<CreateMlJobResponseDTO> createMlJob(
            @Parameter(description = "Unique request identifier", required = true)
            @RequestParam("requestId") UUID requestId,

            @Parameter(description = "Unique record identifier", required = true)
            @RequestParam("recordId") UUID recordId,

            @Parameter(description = "Audio/Video file to process", required = true)
            @RequestPart("file") MultipartFile file,

            @RequestPart("options") MlJobOptions options
            ) {


        // TODO: mapper
        CreateMlJobCommand command = CreateMlJobCommand.builder()
                .requestId(requestId)
                .recordId(recordId)
                .file(file)
                .diarization(options.diarization())
                .summary(options.summary())
                .tasks(options.tasks())
                .build();

        mlJobService.createJob(command);

        return ResponseEntity.accepted().body(
                new CreateMlJobResponseDTO(
                        requestId,
                        recordId,
                        MlJobStatus.QUEUED
                )
        );
    }

    @GetMapping("/{jobId}")
    @Operation(
            summary = "Get ML job status",
            description = "Retrieves the current status of an ML job by ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "ML job status retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MlJobStatusResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ML job not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<MlJobStatusResponseDTO> getJobStatus(@PathVariable UUID jobId) {
        return ResponseEntity.ok(mapper.toStatusDto(mlJobService.getJob(jobId)));
    }

    @GetMapping("/{jobId}/result")
    @Operation(
            summary = "Get completed job result",
            description = "Get result of completed job by ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Completed ML job result retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MlJobResultResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "ML job not found or not completed",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<MlJobResultResponseDTO> getJobResult(@PathVariable UUID jobId) {
        return ResponseEntity.ok(mapper.toResultDto(mlJobService.getCompletedJob(jobId)));
    }
}
