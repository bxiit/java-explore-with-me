package ru.practicum.explorewithme.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class DebeziumConnectorConfig {

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    @Bean
    public io.debezium.config.Configuration eventConfiguration() throws IOException {
        File offsetStorageTempFile = File.createTempFile("offsets_", ".dat");
        File dbHistoryTempFile = File.createTempFile("dbhistory_", ".dat");
        return io.debezium.config.Configuration.create()
                .with("name", "event-postgres-connector")
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                .with("offset.storage.file.filename", offsetStorageTempFile.getAbsolutePath())
                .with("database.server.name", "event-postgres-db-server")
                .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
                .with("database.history.file.filename", dbHistoryTempFile.getAbsolutePath())
                .with("database.hostname", "localhost")
                .with("database.port", 5555)
                .with("database.user", databaseUsername)
                .with("database.password", databasePassword)
                .with("database.dbname", "ewm")
                .with("schema.include.list", "ewm_service")
                .with("topic.prefix", "ewm-users")
                .with("plugin.name", "pgoutput")
                .build();
    }
}
