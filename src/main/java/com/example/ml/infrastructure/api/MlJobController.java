package com.example.ml.infrastructure.api;

import com.example.ml.core.model.MlJob;
import com.example.ml.core.model.MlJobStatus;
import com.example.ml.core.service.CreateMlJobCommand;
import com.example.ml.core.service.MlJobService;
import com.example.ml.infrastructure.api.dto.JobOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ml/jobs")
@RequiredArgsConstructor
public class MlJobController {
    private final MlJobService mlJobService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createMlJob(
            @RequestParam("requestId") UUID requestId,
            @RequestParam("recordId") UUID recordId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("options") JobOptions options,
            @RequestParam("callbackUrl") String callbackUrl
            ) {


        // TODO: mapper
        CreateMlJobCommand command = CreateMlJobCommand.builder()
                .requestId(requestId)
                .recordId(recordId)
                .file(file)
                .diarization(options.diarization())
                .summary(options.summary())
                .tasks(options.tasks())
                .callbackUrl(callbackUrl)
                .build();

        mlJobService.createJob(command);
        // TODO: response DTO
        Map<String, Object> response = Map.of(
                "jobId", requestId,
                "recordId", recordId,
                "status", "QUEUED"
        );
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<Map<String, Object>> getJobStatus(@PathVariable UUID jobId) {
        MlJob job = mlJobService.getJob(jobId);

        // TODO: status DTO
        return ResponseEntity.ok(Map.of(
                "jobId", job.getJobId(),
                "recordId", job.getRecordId(),
                "status", job.getStatus().name(),
                "progress", 42
        ));
    }

    @GetMapping("/{jobId}/result")
    public ResponseEntity<Map<String, Object>> getJobResult(@PathVariable UUID jobId) {
        // TODO: getCompletedJob method in service
        MlJob job = mlJobService.getJob(jobId);

        if (job.getStatus() != MlJobStatus.COMPLETED) {
            return ResponseEntity.badRequest().body(Map.of("error", "Job is not completed yet"));
        }


        // TODO: result DTO
        return ResponseEntity.ok(Map.of(
                "jobId", job.getJobId(),
                "recordId", job.getRecordId(),
                "status", job.getStatus().name(),
                "transcript", job.getMlResult().transcript(),
                "analysis", job.getMlResult().analysis()
        ));
    }
}
