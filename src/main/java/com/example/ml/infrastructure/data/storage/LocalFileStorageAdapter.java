package com.example.ml.infrastructure.data.storage;

import com.example.ml.core.port.outgoing.FileStoragePort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class LocalFileStorageAdapter implements FileStoragePort {
    private final String BASE_DIR = "/tmp/ml_storage/";

    @Override
    public String save(InputStream fileStream, UUID jobId) {
        try {
            Path uploadPath = Paths.get(BASE_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path targetLocation = uploadPath.resolve(jobId.toString());
            Files.copy(fileStream, targetLocation);

            return targetLocation.toAbsolutePath().toString();
        } catch (IOException e) {
            // TODO: exception
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(String path) {

    }
}
