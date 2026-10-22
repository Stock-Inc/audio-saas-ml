package com.example.ml.core.port.outgoing;

import com.example.ml.core.model.MlJob;

import java.util.Optional;
import java.util.UUID;

public interface MlJobDatabasePort {
    void save(MlJob job);
    Optional<MlJob> findById(UUID jobId);
}
