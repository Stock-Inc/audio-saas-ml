package com.example.ml.core.pipeline;

import com.example.ml.core.model.MlJob;

public interface JobProcessingPipeline {
    void processJobAsync(MlJob job);
}
