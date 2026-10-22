package com.example.ml.infrastructure.api;

import com.example.ml.core.model.MlJob;
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
@RequiredArgsConstructor
public class MlJobController {
    private final MlJobService mlJobService;

    @PostMapping(value = "/api/v1/ml/jobs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
}
