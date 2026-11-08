package com.example.ml.core.port.outgoing;

import java.io.InputStream;
import java.util.UUID;

public interface FileStoragePort {
    String save(InputStream fileStream, UUID jobId);
    void delete(String path);
}
