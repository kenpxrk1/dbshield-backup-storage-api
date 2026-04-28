package dbshield.backup.storage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "backup.storage")
public class BackupStorageProperties {

    private String bucket;
    private String endpoint;
    private String accessKey;
    private String secretKey;
}
