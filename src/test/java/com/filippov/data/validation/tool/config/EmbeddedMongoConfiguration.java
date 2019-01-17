package com.filippov.data.validation.tool.config;

import com.mongodb.MongoClient;
import cz.jirutka.spring.embedmongo.EmbeddedMongoBuilder;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
public class EmbeddedMongoConfiguration extends AbstractMongoConfiguration {

    @Override
    @SneakyThrows
    public MongoClient mongoClient() {
        // TODO : move to test configuration
        return new EmbeddedMongoBuilder()
                .version("2.6.1")
                .bindIp("127.0.0.1")
                .port(12345)
                .build();
    }

    @Override
    protected String getDatabaseName() {
        return "dvt";
    }
}
