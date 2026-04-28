package dbshield.backup.storage.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class MinioBackupStorageService implements BackupStorageService {

    private static final String OBJECT_KEY_TEMPLATE = "backups/%s.dump";

    private final MinioClient minioClient;
    private final String bucket;

    @Override
    public String upload(UUID jobId, Path file) {
        if (!Files.isRegularFile(file)) {
            throw new IllegalArgumentException("Backup file does not exist or is not a regular file: " + file);
        }

        String objectKey = buildObjectKey(jobId);
        log.info("Uploading backup to MinIO. bucket={}, objectKey={}, file={}", bucket, objectKey, file);

        try {
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .filename(file.toString())
                            .contentType("application/octet-stream")
                            .build()
            );
            log.info("Backup upload completed. bucket={}, objectKey={}", bucket, objectKey);
            return objectKey;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to upload backup file '" + file + "' to bucket '" + bucket + "' as object '" + objectKey + "'",
                    e
            );
        }
    }

    @Override
    public InputStream download(String objectKey) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to download object '" + objectKey + "' from bucket '" + bucket + "'",
                    e
            );
        }
    }

    @Override
    public void delete(String objectKey) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to delete object '" + objectKey + "' from bucket '" + bucket + "'",
                    e
            );
        }
    }

    private String buildObjectKey(UUID jobId) {
        return OBJECT_KEY_TEMPLATE.formatted(jobId);
    }
}
