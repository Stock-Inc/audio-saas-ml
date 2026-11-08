package com.example.ml.infrastructure.client.mapper;

import com.example.ml.core.model.MlResult;
import com.example.ml.infrastructure.client.dto.WhisperResponse;
import com.example.ml.infrastructure.client.dto.WhisperSegmentDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MlResultMapper {
    List<MlResult.Segment> toDomainSegments(List<WhisperSegmentDto> segmentDtoList);

    @Mapping(target = "id", ignore = true)
    MlResult.Segment toDomainSegment(WhisperSegmentDto segmentDto);

    @AfterMapping
    default void generateIds(@MappingTarget List<MlResult.Segment> targetList) {
        if (targetList == null) return;

        int counter = 1;
        for (int i = 0; i < targetList.size(); i++) {
            MlResult.Segment oldSegment = targetList.get(i);
            MlResult.Segment updatedSegment = new MlResult.Segment(
                    counter++,
                    oldSegment.speaker(),
                    oldSegment.start(),
                    oldSegment.end(),
                    oldSegment.text()
            );
            targetList.set(i, updatedSegment);
        }
    }

}
