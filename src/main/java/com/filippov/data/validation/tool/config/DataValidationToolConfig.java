package com.filippov.data.validation.tool.config;

import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.factory.ColumnDataCacheFactory;
import com.filippov.data.validation.tool.factory.DataStorageFactory;
import com.filippov.data.validation.tool.factory.DatasourceFactory;
import com.filippov.data.validation.tool.factory.DefaultColumnDataCacheFactory;
import com.filippov.data.validation.tool.factory.DefaultDataStorageFactory;
import com.filippov.data.validation.tool.factory.DefaultDatasourceFactory;
import com.filippov.data.validation.tool.metadata.MetadataBinder;
import com.filippov.data.validation.tool.metadata.RuntimeMetadataBinder;
import com.filippov.data.validation.tool.metadata.uuid.RandomUuidRuntimeGenerator;
import com.filippov.data.validation.tool.metadata.uuid.UuidGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class DataValidationToolConfig {

    @Bean
    DatasourceFactory datasourceFactory() {
        return new DefaultDatasourceFactory();
    }

    @Bean
    MetadataBinder metadataBinder(UuidGenerator uuidGenerator) {
        return new RuntimeMetadataBinder(uuidGenerator);
    }

    @Bean
    ColumnDataCacheFactory columnDataCacheFactory() {
        return new DefaultColumnDataCacheFactory();
    }

    @Bean
    DataStorageFactory dataStorageFactory(ColumnDataCacheFactory columnDataCacheFactory) {
        return new DefaultDataStorageFactory(columnDataCacheFactory);
    }

    @Bean
    ColumnDataCache columnDataCache() {
        return new InMemoryColumnDataCache();
    }

    @Bean
    UuidGenerator uuidGenerator() {
        return new RandomUuidRuntimeGenerator();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        // TODO : remove for non-dev environments
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}
