package suzzingv.suzzingv.rockstar.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "dev")
public class DataInitializer {

    private final DataSource dataSource;

    @PostConstruct
    public void initializeData() {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("init.sql"));
            log.info("Sample data initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize sample data", e);
        }
    }
}