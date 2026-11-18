package com.example.ml.infrastructure.client.mapper;

import com.example.ml.core.model.MlResult;
import com.example.ml.infrastructure.client.dto.QwenAnalysisResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = List.class)
public interface QwenAnalysisMapper {

    @Mapping(target = "summary", expression = "java(summary ? response.summary() : null)")
    @Mapping(target = "topics", expression = "java(summary ? response.topics() : List.of())")
    @Mapping(target = "keyPoints", expression = "java(summary ? response.keyPoints() : List.of())")
    @Mapping(target = "decisions", expression = "java(summary ? response.decisions() : List.of())")
    @Mapping(target = "tasks", expression = "java(tasks ? toDomainTasks(response.tasks()) : List.of())")
    MlResult.Analysis toDomainAnalysis(QwenAnalysisResponse response, boolean summary, boolean tasks);

    List<MlResult.TaskItem> toDomainTasks(List<QwenAnalysisResponse.Task> tasks);

    MlResult.TaskItem toDomainTask(QwenAnalysisResponse.Task task);
}
