package dbshield.backup.storage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DbshieldBackupStorageApiApplicationTests.TestApplication.class)
class DbshieldBackupStorageApiApplicationTests {

    @Test
    void contextLoads() {
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class TestApplication {
    }
}
