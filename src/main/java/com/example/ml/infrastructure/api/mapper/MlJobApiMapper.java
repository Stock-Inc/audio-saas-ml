package com.example.ml.infrastructure.api.mapper;

import com.example.ml.core.model.MlJob;
import com.example.ml.infrastructure.api.dto.MlJobResultResponseDTO;
import com.example.ml.infrastructure.api.dto.MlJobStatusResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MlJobApiMapper {
    @Mapping(target = "transcript", source = "mlResult.transcript")
    @Mapping(target = "analysis", source = "mlResult.analysis")
    MlJobResultResponseDTO toResultDto(MlJob job);

    MlJobStatusResponseDTO toStatusDto(MlJob job);
}
