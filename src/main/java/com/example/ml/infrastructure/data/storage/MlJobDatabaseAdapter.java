package com.example.ml.infrastructure.data.storage;

import com.example.ml.core.model.MlJob;
import com.example.ml.core.port.outgoing.MlJobDatabasePort;
import com.example.ml.infrastructure.data.entity.MlJobEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MlJobDatabaseAdapter implements MlJobDatabasePort {
    private final SpringDataMlJobRepository repository;

    @Override
    public void save(MlJob job) {
        // TODO: mapper
        MlJobEntity entity = MlJobEntity.builder()
                .jobId(job.getJobId())
                .recordId(job.getRecordId())
                .status(job.getStatus())
                .fileUrl(job.getFileUrl())
                .fileName(job.getFileName())
                .fileContentType(job.getFileContentType())
                .diarization(job.isDiarization())
                .summary(job.isSummary())
                .tasks(job.isTasks())
                .callbackUrl(job.getCallbackUrl())
                .build();

        repository.save(entity);
    }

    @Override
    public Optional<MlJob> findById(UUID jobId) {
        // TODO: mapper
        return repository.findById(jobId)
                .map(entity -> MlJob.builder()
                        .jobId(entity.getJobId())
                        .recordId(entity.getRecordId())
                        .status(entity.getStatus())
                        .fileUrl(entity.getFileUrl())
                        .fileName(entity.getFileName())
                        .fileContentType(entity.getFileContentType())
                        .diarization(entity.isDiarization())
                        .summary(entity.isSummary())
                        .tasks(entity.isTasks())
                        .callbackUrl(entity.getCallbackUrl())
                        .mlResult(entity.getMlResult())
                        .build());
    }


}
