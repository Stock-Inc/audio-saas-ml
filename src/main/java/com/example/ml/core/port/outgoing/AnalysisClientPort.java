package com.example.ml.core.port.outgoing;

import com.example.ml.core.model.MlResult;

public interface AnalysisClientPort {

    MlResult.Analysis analyze(MlResult.Transcript transcript, boolean summary, boolean tasks);
}
