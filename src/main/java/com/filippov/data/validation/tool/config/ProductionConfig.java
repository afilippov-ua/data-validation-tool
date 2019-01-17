package com.filippov.data.validation.tool.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductionConfig {

    @Bean
    public MongoDatabase applicationDatabase() {
        final String host = "localhost";  // TODO: config holder
        final int port = 27017;  // TODO: config holder
        final String dbName = "dvt"; // TODO: config holder
        return new MongoClient(host, port).getDatabase(dbName);
    }
}
