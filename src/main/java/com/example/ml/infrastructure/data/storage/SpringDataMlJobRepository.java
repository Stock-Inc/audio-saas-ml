package com.example.ml.infrastructure.data.storage;

import com.example.ml.infrastructure.data.entity.MlJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataMlJobRepository extends JpaRepository<MlJobEntity, UUID> {
    @Query("""
        SELECT e FROM MlJobEntity e
        WHERE e.jobId = :jobId
        AND e.status = com.example.ml.core.model.MlJobStatus.COMPLETED
        """)
    Optional<MlJobEntity> findCompletedById(UUID jobId);
}
