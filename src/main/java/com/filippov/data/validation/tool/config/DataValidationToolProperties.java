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

import com.filippov.data.validation.tool.cache.CacheConfig;
import com.filippov.data.validation.tool.cache.EvictionStrategy;
import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Getter
@Setter
@Component
@ConfigurationProperties("data-validation-tool")
public class DataValidationToolProperties {

    @NestedConfigurationProperty
    private Map<DatasourceType, DatasourceProperties> datasources = new HashMap<>();

    public Map<DatasourceType, CacheConfig> getCacheConfiguration() {
        return datasources.entrySet().stream()
                .collect(toMap(
                        Map.Entry::getKey,
                        entry -> CacheConfig.builder()
                                .evictionStrategy(entry.getValue().getCacheConfig().getEvictionStrategy())
                                .maxNumberOfElementsInCache(entry.getValue().getCacheConfig().getMaxNumberOfElementsInCache())
                                .build()));
    }

    @Getter
    @Setter
    public static class DatasourceProperties {
        private CacheConfigProperties cacheConfig;
    }

    @Getter
    @Setter
    public static class CacheConfigProperties {
        private EvictionStrategy evictionStrategy;
        private Integer maxNumberOfElementsInCache;
    }
}
