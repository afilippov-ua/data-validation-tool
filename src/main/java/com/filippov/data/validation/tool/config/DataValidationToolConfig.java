package com.filippov.data.validation.tool.config;

import com.filippov.data.validation.tool.Context;
import com.filippov.data.validation.tool.repository.DefaultStoragePairRepository;
import com.filippov.data.validation.tool.repository.StoragePairRepository;
import com.filippov.data.validation.tool.storage.ApplicationStorage;
import com.filippov.data.validation.tool.storage.MongoApplicationStorage;
import com.filippov.data.validation.tool.storage.mapper.ApplicationStorageBsonMapper;
import com.filippov.data.validation.tool.storage.mapper.DatasourceDtoMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataValidationToolConfig {

    @Bean
    public StoragePairRepository storagePairRepository(ApplicationStorage applicationStorage, DatasourceDtoMapper mapper) {
        return new DefaultStoragePairRepository(applicationStorage, mapper);
    }

    @Bean
    public Context context(StoragePairRepository storagePairRepository) {
        return new Context(storagePairRepository);
    }

    @Bean
    public MongoDatabase applicationDatabase() {
        final String host = "localhost";  // TODO: config holder
        final int port = 27017;  // TODO: config holder
        final String dbName = "dvt"; // TODO: config holder
        return new MongoClient(host, port).getDatabase(dbName);
    }

    @Bean
    public ApplicationStorageBsonMapper applicationStorageDtoMapper() {
        return new ApplicationStorageBsonMapper();
    }

    @Bean
    public DatasourceDtoMapper datasourceDtoMapper() {
        return new DatasourceDtoMapper();
    }

    @Bean
    public ApplicationStorage applicationStorage(MongoDatabase applicationDatabase, ApplicationStorageBsonMapper mapper) {
        return new MongoApplicationStorage(applicationDatabase, mapper);
    }
}
