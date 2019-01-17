package com.filippov.data.validation.tool.config;

import com.filippov.data.validation.tool.Context;
import com.filippov.data.validation.tool.repository.DefaultStoragePairRepository;
import com.filippov.data.validation.tool.repository.StoragePairRepository;
import com.filippov.data.validation.tool.storage.ApplicationStorage;
import com.filippov.data.validation.tool.storage.MongoApplicationStorage;
import com.filippov.data.validation.tool.storage.mapper.MongoDtoBsonMapper;
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
    public MongoDtoBsonMapper mongoDtoBsonMapper() {
        return new MongoDtoBsonMapper();
    }

    @Bean
    public StoragePairRepository storagePairRepository(ApplicationStorage applicationStorage, MongoDtoBsonMapper mongoDtoBsonMapper) {
        return new DefaultStoragePairRepository(applicationStorage, mongoDtoBsonMapper);
    }

    @Bean
    public ApplicationStorage applicationStorage(MongoDatabase applicationDatabase, MongoDtoBsonMapper mapper) {
        return new MongoApplicationStorage(applicationDatabase, mapper);
    }
}
