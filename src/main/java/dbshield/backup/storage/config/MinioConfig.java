package dbshield.backup.storage.config;

import dbshield.backup.storage.service.BackupStorageService;
import dbshield.backup.storage.service.MinioBackupStorageService;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BackupStorageProperties.class)
public class MinioConfig {

    private final BackupStorageProperties props;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(requireConfigured(props.getEndpoint(), "endpoint"))
                .credentials(
                        requireConfigured(props.getAccessKey(), "access-key"),
                        requireConfigured(props.getSecretKey(), "secret-key")
                )
                .build();
    }

    @Bean
    public BackupStorageService backupStorageService(MinioClient client) {
        return new MinioBackupStorageService(client, requireConfigured(props.getBucket(), "bucket"));
    }

    private String requireConfigured(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required property: backup.storage." + name);
        }
        return value;
    }
}
