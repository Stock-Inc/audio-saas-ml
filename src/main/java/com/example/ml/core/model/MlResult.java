package com.example.ml.core.model;

import java.util.List;

public record MlResult(
        Transcript transcript,
        Analysis analysis
) {

    public record Transcript(
        List<Segment> segments
    ) {}

    public record Segment(
            Integer id,
            String speaker,
            Double start,
            Double end,
            String text
    ) {}

    public record Analysis(
            String summary,
            List<String> topics,
            List<String> keyPoints,
            List<String> decisions,
            List<TaskItem> tasks
    ) {}

    public record TaskItem(
            String text,
            String assignee
    ) {}
}