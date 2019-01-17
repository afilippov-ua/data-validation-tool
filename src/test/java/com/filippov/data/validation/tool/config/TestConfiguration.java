package com.filippov.data.validation.tool.config;

import com.filippov.data.validation.tool.Context;
import com.filippov.data.validation.tool.repository.DefaultStoragePairRepository;
import com.filippov.data.validation.tool.repository.StoragePairRepository;
import com.filippov.data.validation.tool.storage.ApplicationStorage;
import com.filippov.data.validation.tool.storage.MongoApplicationStorage;
import com.filippov.data.validation.tool.storage.mapper.ApplicationStorageBsonMapper;
import com.filippov.data.validation.tool.storage.mapper.DatasourceDtoMapper;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;

@Configuration
public class TestConfiguration {

    @Bean
    public StoragePairRepository storagePairRepository(ApplicationStorage applicationStorage, DatasourceDtoMapper mapper) {
        return new DefaultStoragePairRepository(applicationStorage, mapper);
    }

    @Bean
    public Context context(StoragePairRepository storagePairRepository) {
        return new Context(storagePairRepository);
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
    public MongoDatabase applicationDatabase(MongoDbFactory mongoDbFactory) {
        return mongoDbFactory.getDb();
    }

    @Bean
    public ApplicationStorage applicationStorage(MongoDatabase applicationDatabase, ApplicationStorageBsonMapper mapper) {
        return new MongoApplicationStorage(applicationDatabase, mapper);
    }
}
