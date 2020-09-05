/*
 *   Copyright 2018-2020 the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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
import com.filippov.data.validation.tool.utils.uuid.RandomUuidRuntimeGenerator;
import com.filippov.data.validation.tool.utils.uuid.UuidGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
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

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
