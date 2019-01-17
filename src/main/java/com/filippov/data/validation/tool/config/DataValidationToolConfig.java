package com.filippov.data.validation.tool.config;

import com.filippov.data.validation.tool.Context;
import com.filippov.data.validation.tool.repository.DefaultStoragePairRepository;
import com.filippov.data.validation.tool.repository.StoragePairRepository;
import com.filippov.data.validation.tool.storage.ApplicationStorage;
import com.filippov.data.validation.tool.storage.MongoApplicationStorage;
import com.filippov.data.validation.tool.storage.mapper.DtoMapper;
import com.filippov.data.validation.tool.storage.mapper.MongoBsonMapper;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataValidationToolConfig {

    @Bean
    public Context context(StoragePairRepository storagePairRepository) {
        return new Context(storagePairRepository);
    }

    @Bean
    public MongoBsonMapper mongoDtoBsonMapper() {
        return new MongoBsonMapper();
    }

    @Bean
    public DtoMapper dtoMapper() {
        return new DtoMapper();
    }

    @Bean
    public StoragePairRepository storagePairRepository(ApplicationStorage applicationStorage, DtoMapper dtoMapper) {
        return new DefaultStoragePairRepository(applicationStorage, dtoMapper);
    }

    @Bean
    public ApplicationStorage applicationStorage(MongoDatabase applicationDatabase, MongoBsonMapper mapper) {
        return new MongoApplicationStorage(applicationDatabase, mapper);
    }
}
