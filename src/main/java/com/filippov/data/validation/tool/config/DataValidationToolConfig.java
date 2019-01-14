package com.filippov.data.validation.tool.config;

import com.filippov.data.validation.tool.Context;
import com.filippov.data.validation.tool.repository.DefaultStoragePairRepository;
import com.filippov.data.validation.tool.repository.StoragePairRepository;
import com.filippov.data.validation.tool.storage.ApplicationStorage;
import com.filippov.data.validation.tool.storage.MongoApplicationStorage;
import com.filippov.data.validation.tool.storage.mapper.ApplicationStorageBsonMapper;
import com.filippov.data.validation.tool.storage.mapper.DatasourceDtoMapper;
import com.mongodb.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan("com.filippov.data.validation.tool.config")
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
    public MongoClient mongoClient() {
        final String host = "localhost";  // TODO: config holder
        final Integer port = 27017;  // TODO: config holder
        return new MongoClient(host, port);
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
    public ApplicationStorage applicationStorage(MongoClient mongoClient, ApplicationStorageBsonMapper mapper) {
        final String dbName = "dvt"; // TODO: config holder
        return new MongoApplicationStorage(mongoClient.getDatabase(dbName), mapper);
    }
}
