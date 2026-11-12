package com.example.ml.infrastructure.data.storage;

import com.example.ml.core.model.MlJob;
import com.example.ml.core.port.outgoing.MlJobDatabasePort;
import com.example.ml.infrastructure.data.entity.MlJobEntity;
import com.example.ml.infrastructure.data.mapper.MlJobEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MlJobDatabaseAdapter implements MlJobDatabasePort {
    private final SpringDataMlJobRepository repository;
    private final MlJobEntityMapper mapper;

    // TODO: exceptions

    @Override
    public void save(MlJob job) {
        MlJobEntity entity = mapper.toMlJobEntity(job);
        repository.save(entity);
    }

    @Override
    public Optional<MlJob> findById(UUID jobId) {
        return repository.findById(jobId)
                .map(mapper::toMlJob);
    }

    @Override
    public Optional<MlJob> findCompletedById(UUID jobId) {
        return repository.findCompletedById(jobId)
                .map(mapper::toMlJob);
    }
}
