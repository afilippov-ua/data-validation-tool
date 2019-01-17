package com.filippov.data.validation.tool.config;

import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;

@Configuration
public class TestConfig {

    @Bean
    public MongoDatabase applicationDatabase(MongoDbFactory mongoDbFactory) {
        return mongoDbFactory.getDb();
    }
}
