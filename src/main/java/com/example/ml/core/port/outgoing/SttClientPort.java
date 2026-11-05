package com.example.ml.core.port.outgoing;

import com.example.ml.core.model.MlResult;

public interface SttClientPort {
    MlResult.Transcript transcribe(String audioFilePath, boolean diarization);
}
