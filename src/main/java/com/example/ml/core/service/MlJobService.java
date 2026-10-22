package com.example.ml.core.service;

import com.example.ml.core.model.MlJob;
import com.example.ml.core.pipeline.MlProcessingPipeline;
import com.example.ml.core.port.outgoing.MlJobDatabasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MlJobService {
    private final MlJobDatabasePort databasePort;
    private final MlProcessingPipeline pipeline;

    public MlJob createJob(CreateMlJobCommand command) {
        UUID jobId = command.requestId();
        MultipartFile file = command.file();

        // TODO: fileStorageService (save file)
        String localFilePath = "/tmp/ml_storage/" + jobId + "_" + file.getOriginalFilename();


        // TODO: mapper
        MlJob job = MlJob.builder()
                .jobId(jobId)
                .recordId(command.recordId())
                .fileUrl(localFilePath)
                .fileName(file.getOriginalFilename())
                .fileContentType(file.getContentType())
                .diarization(command.diarization())
                .summary(command.summary())
                .tasks(command.tasks())
                .callbackUrl(command.callbackUrl())
                .build();

        databasePort.save(job);
        pipeline.processJobAsync(job);
        return job;
    }

    // TODO: exceptions (handler, custom ...)
    public MlJob getJob(UUID jobId) {
        return databasePort.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found: " + jobId));
    }

    // TODO: getCompletedJob method
}
