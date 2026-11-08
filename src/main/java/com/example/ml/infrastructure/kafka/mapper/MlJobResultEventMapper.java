package com.example.ml.infrastructure.kafka.mapper;

import com.example.ml.core.model.MlJob;
import com.example.ml.core.model.MlResult;
import com.example.ml.infrastructure.kafka.dto.Analysis;
import com.example.ml.infrastructure.kafka.dto.MlJobResultEvent;
import com.example.ml.infrastructure.kafka.dto.Segment;
import com.example.ml.infrastructure.kafka.dto.Task;
import com.example.ml.infrastructure.kafka.dto.Transcript;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface MlJobResultEventMapper {
    @Mapping(target = "transcript", source = "mlResult.transcript")
    @Mapping(target = "analysis", source = "mlResult.analysis")
    MlJobResultEvent toEvent(MlJob job);

    Transcript toTranscript(MlResult.Transcript transcript);

    Segment toSegment(MlResult.Segment segment);

    Analysis toAnalysis(MlResult.Analysis analysis);

    Task toTask(MlResult.TaskItem task);

    List<Segment> toSegments(List<MlResult.Segment> segments);

    List<Task> toTasks(List<MlResult.TaskItem> tasks);

    List<CharSequence> toStrings(List<String> values);

    default CharSequence toCharSequence(UUID value) {
        return value == null ? null : value.toString();
    }
}
