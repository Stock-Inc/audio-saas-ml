package com.example.ml.core.service;

import com.example.ml.core.model.MlJob;
import com.example.ml.core.pipeline.MlJobProcessingPipeline;
import com.example.ml.core.port.outgoing.FileStoragePort;
import com.example.ml.core.port.outgoing.MlJobDatabasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MlJobService {
    private final MlJobDatabasePort databasePort;
    private final FileStoragePort fileStoragePort;
    private final MlJobProcessingPipeline pipeline;

    // TODO: exceptions

    public MlJob createJob(CreateMlJobCommand command) {
        UUID jobId = command.requestId();
        MultipartFile file = command.file();

        String fileUrl;
        try {
            InputStream inputStream = file.getInputStream();
            fileUrl = fileStoragePort.save(inputStream, jobId);
        } catch (IOException e) {
            throw new RuntimeException();
        }


        // TODO: mapper
        MlJob job = MlJob.builder()
                .jobId(jobId)
                .recordId(command.recordId())
                .fileUrl(fileUrl)
                .fileName(file.getOriginalFilename())
                .fileContentType(file.getContentType())
                .diarization(command.diarization())
                .summary(command.summary())
                .tasks(command.tasks())
                .build();

        databasePort.save(job);
        pipeline.processJobAsync(job);
        return job;
    }

    public MlJob getJob(UUID jobId) {
        return databasePort.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found: " + jobId));
    }

    public MlJob getCompletedJob(UUID jobId) {
        return databasePort.findCompletedById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not completed yet"));
    }
}
