package dbshield.backup.storage.service;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

public interface BackupStorageService {

    String upload(UUID jobId, Path file);

    InputStream download(String objectKey);

    void delete(String objectKey);
}
