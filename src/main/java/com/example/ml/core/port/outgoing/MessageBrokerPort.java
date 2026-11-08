package com.example.ml.core.port.outgoing;

import com.example.ml.core.model.MlJob;

public interface MessageBrokerPort {
    void publish(MlJob mlJob);
}
