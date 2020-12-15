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

import com.filippov.data.validation.tool.AbstractUnitTest;
import com.filippov.data.validation.tool.columndatacache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.config.DataValidationToolProperties;
import com.filippov.data.validation.tool.datasource.JsonDatasource;
import com.filippov.data.validation.tool.datasource.JsonDatasourceConfig;
import com.filippov.data.validation.tool.datasource.TestInMemoryDatasource;
import com.filippov.data.validation.tool.datasource.TestInMemoryDatasourceConfig;
import com.filippov.data.validation.tool.model.ColumnDataCache;
import com.filippov.data.validation.tool.model.cache.CacheConfig;
import com.filippov.data.validation.tool.model.cache.EvictionStrategy;
import com.filippov.data.validation.tool.model.datasource.DatasourceType;
import com.filippov.data.validation.tool.model.datastorage.RelationType;
import com.filippov.data.validation.tool.model.workspace.Workspace;
import com.filippov.data.validation.tool.repository.redis.repository.RedisCacheInfoRepository;
import com.filippov.data.validation.tool.repository.redis.repository.RedisColumnDataRepository;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static com.filippov.data.validation.tool.model.datasource.DatasourceType.JSON_DATASOURCE;
import static com.filippov.data.validation.tool.model.datasource.DatasourceType.TEST_IN_MEMORY_DATASOURCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class DefaultColumnDataCacheFactoryTest extends AbstractUnitTest {
    private final Map<DatasourceType, CacheConfig> cacheConfigMap = Map.of(
            TEST_IN_MEMORY_DATASOURCE, CacheConfig.builder()
                    .evictionStrategy(EvictionStrategy.FIFO)
                    .maxNumberOfElementsInCache(1000)
                    .build(),
            JSON_DATASOURCE, CacheConfig.builder()
                    .evictionStrategy(EvictionStrategy.FIFO)
                    .maxNumberOfElementsInCache(1000)
                    .build());

    @Test
    void getOrCreateCacheForJsonDatasourceTest() {
        final RedisColumnDataRepository redisColumnDataRepository = Mockito.mock(RedisColumnDataRepository.class);
        final RedisCacheInfoRepository redisCacheInfoRepository = Mockito.mock(RedisCacheInfoRepository.class);
        final DataValidationToolProperties properties = Mockito.mock(DataValidationToolProperties.class);
        when(properties.getDatasourceCacheConfiguration()).thenReturn(
                ImmutableMap.<DatasourceType, CacheConfig>builder()
                        .put(TEST_IN_MEMORY_DATASOURCE, CacheConfig.DEFAULT_CONFIG)
                        .put(JSON_DATASOURCE, CacheConfig.DEFAULT_CONFIG)
                        .build());
        when(properties.getDefaultCacheConfig()).thenReturn(CacheConfig.DEFAULT_CONFIG);

        final DefaultColumnDataCacheFactory factory = new DefaultColumnDataCacheFactory(properties, redisColumnDataRepository, redisCacheInfoRepository);

        final ColumnDataCache columnDataCache = factory.getOrCreateForDatasource(
                Workspace.builder().id("test-ws").build(),
                new JsonDatasource(
                        JsonDatasourceConfig.builder()
                                .metadataFilePath(LEFT_DS_METADATA_PATH)
                                .dataFilePath(LEFT_DS_DATA_PATH)
                                .maxConnections(1)
                                .build()),
                RelationType.LEFT);

        assertThat(columnDataCache)
                .isNotNull()
                .isInstanceOf(InMemoryColumnDataCache.class);
    }

    @Test
    void getOrCreateCacheForTestInMemoryDatasourceTest() {
        final RedisColumnDataRepository redisColumnDataRepository = Mockito.mock(RedisColumnDataRepository.class);
        final RedisCacheInfoRepository redisCacheInfoRepository = Mockito.mock(RedisCacheInfoRepository.class);
        final DataValidationToolProperties properties = Mockito.mock(DataValidationToolProperties.class);
        when(properties.getDatasourceCacheConfiguration()).thenReturn(
                ImmutableMap.<DatasourceType, CacheConfig>builder()
                        .put(TEST_IN_MEMORY_DATASOURCE, CacheConfig.DEFAULT_CONFIG)
                        .put(JSON_DATASOURCE, CacheConfig.DEFAULT_CONFIG)
                        .build());

        final DefaultColumnDataCacheFactory factory = new DefaultColumnDataCacheFactory(properties, redisColumnDataRepository, redisCacheInfoRepository);

        final ColumnDataCache columnDataCache = factory.getOrCreateForDatasource(
                Workspace.builder().id("test-ws").build(),
                new TestInMemoryDatasource(TestInMemoryDatasourceConfig.builder()
                        .relationType(RelationType.LEFT)
                        .maxConnections(1)
                        .build()),
                RelationType.LEFT);

        assertThat(columnDataCache)
                .isNotNull()
                .isInstanceOf(InMemoryColumnDataCache.class);
    }

    @Test
    void incorrectInputTest() {
        final RedisColumnDataRepository redisColumnDataRepository = Mockito.mock(RedisColumnDataRepository.class);
        final RedisCacheInfoRepository redisCacheInfoRepository = Mockito.mock(RedisCacheInfoRepository.class);
        final DataValidationToolProperties properties = Mockito.mock(DataValidationToolProperties.class);
        when(properties.getDatasourceCacheConfiguration()).thenReturn(
                ImmutableMap.<DatasourceType, CacheConfig>builder()
                        .put(TEST_IN_MEMORY_DATASOURCE, CacheConfig.DEFAULT_CONFIG)
                        .put(JSON_DATASOURCE, CacheConfig.DEFAULT_CONFIG)
                        .build());

        final DefaultColumnDataCacheFactory factory = new DefaultColumnDataCacheFactory(properties, redisColumnDataRepository, redisCacheInfoRepository);

        assertThatThrownBy(() -> factory.getOrCreateForDatasource(
                Workspace.builder().id("test-ws").build(),
                null,
                RelationType.LEFT))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
