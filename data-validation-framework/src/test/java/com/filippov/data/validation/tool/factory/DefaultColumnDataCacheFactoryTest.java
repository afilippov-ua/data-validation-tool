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

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.cache.CacheConfig;
import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.cache.EvictionStrategy;
import com.filippov.data.validation.tool.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.datasource.jsondatasource.JsonDatasource;
import com.filippov.data.validation.tool.datasource.jsondatasource.JsonDatasourceConfig;
import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import com.filippov.data.validation.tool.datasource.testinmemorydatasource.TestInMemoryDatasource;
import com.filippov.data.validation.tool.datasource.testinmemorydatasource.TestInMemoryDatasourceConfig;
import com.filippov.data.validation.tool.datastorage.RelationType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.filippov.data.validation.tool.datasource.model.DatasourceType.JSON_DATASOURCE;
import static com.filippov.data.validation.tool.datasource.model.DatasourceType.TEST_IN_MEMORY_DATASOURCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DefaultColumnDataCacheFactoryTest extends AbstractTest {
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
        final DefaultColumnDataCacheFactory factory = new DefaultColumnDataCacheFactory(cacheConfigMap);

        final ColumnDataCache columnDataCache = factory.getOrCreateForDatasource(
                new JsonDatasource(
                        JsonDatasourceConfig.builder()
                                .metadataFilePath(LEFT_DS_METADATA_PATH)
                                .dataFilePath(LEFT_DS_DATA_PATH)
                                .maxConnections(1)
                                .build()));

        assertThat(columnDataCache)
                .isNotNull()
                .isInstanceOf(InMemoryColumnDataCache.class);
    }

    @Test
    void getOrCreateCacheForTestInMemoryDatasourceTest() {
        final DefaultColumnDataCacheFactory factory = new DefaultColumnDataCacheFactory(cacheConfigMap);

        final ColumnDataCache columnDataCache = factory.getOrCreateForDatasource(
                new TestInMemoryDatasource(TestInMemoryDatasourceConfig.builder()
                        .relationType(RelationType.LEFT)
                        .maxConnections(1)
                        .build()));

        assertThat(columnDataCache)
                .isNotNull()
                .isInstanceOf(InMemoryColumnDataCache.class);
    }

    @Test
    void incorrectInputTest() {
        final DefaultColumnDataCacheFactory factory = new DefaultColumnDataCacheFactory(cacheConfigMap);

        assertThatThrownBy(() -> factory.getOrCreateForDatasource(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
