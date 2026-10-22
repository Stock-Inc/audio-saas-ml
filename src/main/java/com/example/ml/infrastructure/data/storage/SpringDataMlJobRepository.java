package com.example.ml.infrastructure.data.storage;

import com.example.ml.infrastructure.data.entity.MlJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataMlJobRepository extends JpaRepository<MlJobEntity, UUID> {
}
