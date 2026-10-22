package com.example.ml.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MlJob {
    private final UUID jobId;
    private final UUID recordId;
    @Builder.Default
    private MlJobStatus status = MlJobStatus.QUEUED;

    private final String fileUrl;
    private final String fileName;
    private final String fileContentType;

    private final boolean diarization;
    private final boolean summary;
    private final boolean tasks;

    private final String callbackUrl;
    @Builder.Default
    private MlResult mlResult = null;

    public void changeStatus(MlJobStatus newStatus) {
        this.status = newStatus;
    }

    public void complete(MlResult mlResult) {
        this.status = MlJobStatus.COMPLETED;
        this.mlResult = mlResult;
    }
}
