package com.example.ml.infrastructure.client.dto;

import java.util.List;

public record QwenAnalysisResponse(
        String summary,
        List<String> topics,
        List<String> keyPoints,
        List<String> decisions,
        List<Task> tasks
) {
    public record Task(
            String text,
            String assignee
    ) {}
}
