package com.example.ml.core.pipeline;

import com.example.ml.core.model.MlJob;

public interface MlJobProcessingPipeline {
    void processJobAsync(MlJob job);
}
