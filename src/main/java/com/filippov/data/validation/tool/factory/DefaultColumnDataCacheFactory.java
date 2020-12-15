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

package com.filippov.data.validation.tool.factory;

import com.filippov.data.validation.tool.columndatacache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.columndatacache.RedisColumnDataCache;
import com.filippov.data.validation.tool.config.DataValidationToolProperties;
import com.filippov.data.validation.tool.model.ColumnDataCache;
import com.filippov.data.validation.tool.model.ColumnDataCacheFactory;
import com.filippov.data.validation.tool.model.Datasource;
import com.filippov.data.validation.tool.model.cache.CacheConfig;
import com.filippov.data.validation.tool.model.datasource.DatasourceType;
import com.filippov.data.validation.tool.model.datastorage.RelationType;
import com.filippov.data.validation.tool.model.workspace.Workspace;
import com.filippov.data.validation.tool.repository.redis.repository.RedisCacheInfoRepository;
import com.filippov.data.validation.tool.repository.redis.repository.RedisColumnDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class DefaultColumnDataCacheFactory implements ColumnDataCacheFactory {

    private final Map<DatasourceType, CacheConfig> cacheConfigs;
    private final CacheConfig defaultCacheConfig;
    private final RedisColumnDataRepository redisColumnDataRepository;
    private final RedisCacheInfoRepository redisCacheInfoRepository;

    public DefaultColumnDataCacheFactory(DataValidationToolProperties applicationProperties,
                                         RedisColumnDataRepository redisColumnDataRepository,
                                         RedisCacheInfoRepository redisCacheInfoRepository) {
        this.cacheConfigs = applicationProperties.getDatasourceCacheConfiguration();
        this.defaultCacheConfig = applicationProperties.getDefaultCacheConfig();
        this.redisColumnDataRepository = redisColumnDataRepository;
        this.redisCacheInfoRepository = redisCacheInfoRepository;
    }

    @Override
    public ColumnDataCache getOrCreateForDatasource(Workspace workspace, Datasource datasource, RelationType relationType) {
        if (datasource == null) {
            throw new IllegalArgumentException("Incorrect input: datasource is null");
        }
        final CacheConfig cacheConfig = cacheConfigs.getOrDefault(datasource.getConfig().getDatasourceType(), defaultCacheConfig);
        switch (cacheConfig.getCacheType()) {
            case IN_MEMORY:
                return new InMemoryColumnDataCache(cacheConfig);
            case REDIS:
                return new RedisColumnDataCache(workspace, relationType, cacheConfig, redisColumnDataRepository, redisCacheInfoRepository);
            default:
                throw new UnsupportedOperationException("Unsupported cache type: " + cacheConfig.getCacheType());
        }
    }
}
