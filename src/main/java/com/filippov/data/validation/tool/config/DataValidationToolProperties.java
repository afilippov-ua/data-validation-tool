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

import com.filippov.data.validation.tool.model.cache.ColumnDataCacheType;
import com.filippov.data.validation.tool.model.cache.EvictionStrategy;
import com.filippov.data.validation.tool.model.datasource.DatasourceType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Setter
@Component
@ConfigurationProperties("data-validation-tool")
public class DataValidationToolProperties {

    @NestedConfigurationProperty
    private Map<DatasourceType, DatasourceConfig> datasources = new HashMap<>();

    @NestedConfigurationProperty
    private CacheConfig defaultColumnDataCacheConfig;

    @NestedConfigurationProperty
    private RedisConfig redisConfig;

    public Map<DatasourceType, com.filippov.data.validation.tool.model.cache.CacheConfig> getDatasourceCacheConfiguration() {
        return datasources.entrySet().stream()
                .collect(toMap(
                        Map.Entry::getKey,
                        entry -> com.filippov.data.validation.tool.model.cache.CacheConfig.builder()
                                .cacheType(entry.getValue().getCacheConfig().getCacheType())
                                .evictionStrategy(entry.getValue().getCacheConfig().getEvictionStrategy())
                                .maxNumberOfElementsInCache(entry.getValue().getCacheConfig().getMaxNumberOfElementsInCache())
                                .build()));
    }

    public com.filippov.data.validation.tool.model.cache.CacheConfig getDefaultCacheConfig() {
        if (defaultColumnDataCacheConfig != null) {
            return com.filippov.data.validation.tool.model.cache.CacheConfig.builder()
                    .cacheType(defaultColumnDataCacheConfig.getCacheType())
                    .evictionStrategy(defaultColumnDataCacheConfig.getEvictionStrategy())
                    .maxNumberOfElementsInCache(defaultColumnDataCacheConfig.getMaxNumberOfElementsInCache())
                    .build();
        } else {
            return com.filippov.data.validation.tool.model.cache.CacheConfig.DEFAULT_CONFIG;
        }
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    @Getter
    @Setter
    public static class DatasourceConfig {
        private CacheConfig cacheConfig;
    }

    @Getter
    @Setter
    public static class CacheConfig {
        private ColumnDataCacheType cacheType;
        private EvictionStrategy evictionStrategy;
        private Integer maxNumberOfElementsInCache;
    }

    @Getter
    @Setter
    public static class RedisConfig {
        private String host;
        private Integer port;
    }
}
