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
import com.filippov.data.validation.tool.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.JsonDatasource;
import com.filippov.data.validation.tool.datasource.TestInMemoryDatasource;
import com.filippov.data.validation.tool.datasource.config.JsonDatasourceConfig;
import com.filippov.data.validation.tool.datasource.config.TestInMemoryDatasourceConfig;
import com.filippov.data.validation.tool.datastorage.DataStorage;
import com.filippov.data.validation.tool.datastorage.RelationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DefaultDataStorageFactoryTest extends AbstractTest {

    private static final JsonDatasource JSON_DATASOURCE = new JsonDatasource(
            JsonDatasourceConfig.builder()
                    .metadataFilePath("test-metadata-file-path")
                    .dataFilePath("test-data-file-path")
                    .maxConnections(1)
                    .build());

    private static final TestInMemoryDatasource TEST_IN_MEMORY_DATASOURCE = new TestInMemoryDatasource(
            TestInMemoryDatasourceConfig.builder()
                    .relationType(RelationType.LEFT)
                    .maxConnections(1)
                    .build());

    private static final ColumnDataCacheFactory COLUMN_DATA_CACHE_FACTORY_MOCK = Mockito.mock(ColumnDataCacheFactory.class);

    static {
        when(COLUMN_DATA_CACHE_FACTORY_MOCK.getOrCreateForDatasource(any())).thenReturn(new InMemoryColumnDataCache());
    }

    static Object[][] correctInputProvider() {
        return new Object[][]{
                {JSON_DATASOURCE, RelationType.LEFT, 1},
                {JSON_DATASOURCE, RelationType.RIGHT, Integer.MAX_VALUE},
                {TEST_IN_MEMORY_DATASOURCE, RelationType.LEFT, 1},
                {TEST_IN_MEMORY_DATASOURCE, RelationType.RIGHT, Integer.MAX_VALUE},
        };
    }

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, RelationType.LEFT, 1},
                {TEST_IN_MEMORY_DATASOURCE, null, Integer.MAX_VALUE},
                {TEST_IN_MEMORY_DATASOURCE, RelationType.LEFT, null},
                {TEST_IN_MEMORY_DATASOURCE, RelationType.LEFT, 0},
                {TEST_IN_MEMORY_DATASOURCE, RelationType.LEFT, -1},
                {TEST_IN_MEMORY_DATASOURCE, RelationType.LEFT, Integer.MIN_VALUE}
        };
    }

    @ParameterizedTest
    @MethodSource("correctInputProvider")
    void defaultDataStorageFactoryTest(Datasource datasource, RelationType relationType, Integer maxConnections) {
        final DataStorage dataStorage = new DefaultDataStorageFactory(COLUMN_DATA_CACHE_FACTORY_MOCK)
                .create(datasource, relationType, maxConnections);

        assertThat(dataStorage).isNotNull();
        assertThat(dataStorage.getDatasource()).isEqualTo(datasource);
        assertThat(dataStorage.getConfig().getRelationType()).isEqualTo(relationType);
        assertThat(dataStorage.getConfig().getMaxConnections()).isEqualTo(maxConnections);
    }

    @Test
    void incorrectColumnDataCacheFactoryTest() {
        assertThatThrownBy(() -> new DefaultDataStorageFactory(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void incorrectInputTest(Datasource datasource, RelationType relationType, Integer maxConnections) {
        assertThatThrownBy(() -> new DefaultDataStorageFactory(COLUMN_DATA_CACHE_FACTORY_MOCK)
                .create(datasource, relationType, maxConnections))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
