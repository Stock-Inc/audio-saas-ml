package com.example.ml.core.port.outgoing;

import java.util.UUID;

public interface WebhookPort {
    void sendCallback(String callbackUrl, UUID jobId, UUID recordId, String status);
}
