package com.uniClub.file;

import com.uniClub.exceptions.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

public abstract class BaseFileUploadService {
    @Value("${app.config.base-url}")
    private String baseUrl;

    public String uploadFile(String base64Content, String fileName, Long entityId) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
            String targetDir = getTargetDirectory();
            Path uploadPath = Paths.get(targetDir);

            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            String uniqueFileName = getPrefix() + "_" + entityId + "_" + UUID.randomUUID() + "_" + fileName;
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.write(filePath, decodedBytes);

            return baseUrl + "/" + targetDir + "/" + uniqueFileName;
        } catch (Exception e) {
            throw new FileStorageException("Dosya yükleme başarısız: " + fileName, e);
        }
    }

    protected abstract String getTargetDirectory();
    protected abstract String getPrefix();
}