package com.example.ml.infrastructure.data.mapper;

import com.example.ml.core.model.MlJob;
import com.example.ml.infrastructure.data.entity.MlJobEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MlJobEntityMapper {
    MlJob toMlJob(MlJobEntity entity);
    MlJobEntity toMlJobEntity(MlJob job);
}
