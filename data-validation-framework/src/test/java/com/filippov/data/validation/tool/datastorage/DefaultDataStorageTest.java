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

package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.model.ColumnDataInfo;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToIntegerTransformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToStringTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class DefaultDataStorageTest extends AbstractTest {

    private static final List<String> IDS = asList("1", "2", "3", "4", "5", "7");
    private static final DataStorageConfig DATA_STORAGE_CONFIG = DataStorageConfig.builder()
            .relationType(LEFT)
            .maxConnections(1)
            .build();

    static Object[][] columnProvider() {
        return new Object[][]{
                {USERS_ID, IDS},
                {USERS_USERNAME, asList("user1", "user2", "user3", "user4", "user5", "user7")},
                {USERS_PASSWORD, asList("pass1", "pass2", "pass3", "pass4", "pass5", "pass7")}
        };
    }

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, LEFT_DATASOURCE, new InMemoryColumnDataCache()},
                {DATA_STORAGE_CONFIG, null, new InMemoryColumnDataCache()},
                {DATA_STORAGE_CONFIG, LEFT_DATASOURCE, null},
        };
    }

    @Test
    void getConfigTest() {
        final DataStorageConfig expectedConfig = DataStorageConfig.builder()
                .relationType(LEFT)
                .maxConnections(1)
                .build();
        ;
        final DefaultDataStorage storage = new DefaultDataStorage(expectedConfig, LEFT_DATASOURCE, new InMemoryColumnDataCache());

        assertThat(storage.getConfig()).isEqualTo(expectedConfig);
    }

    @Test
    void getDatasourceTest() {
        final DefaultDataStorage storage = new DefaultDataStorage(
                DataStorageConfig.builder()
                        .relationType(LEFT)
                        .maxConnections(1)
                        .build(),
                LEFT_DATASOURCE,
                new InMemoryColumnDataCache());

        assertThat(storage.getDatasource()).isEqualTo(LEFT_DATASOURCE);
    }

    @ParameterizedTest()
    @MethodSource("columnProvider")
    void getDataTest(String columnName, List<Object> expectedValues) {
        final DefaultDataStorage storage = new DefaultDataStorage(
                DataStorageConfig.builder()
                        .relationType(LEFT)
                        .maxConnections(1)
                        .build(),
                LEFT_DATASOURCE,
                new InMemoryColumnDataCache());

        final DatasourceTable leftTable = LEFT_DATASOURCE.getMetadata().getTableByName(USERS);
        final DatasourceTable rightTable = RIGHT_DATASOURCE.getMetadata().getTableByName(USERS);

        final DatasourceColumn leftKeyColumn = LEFT_DATASOURCE.getMetadata().getColumnByName(leftTable.getName(), USERS_ID);
        final DatasourceColumn rightKeyColumn = RIGHT_DATASOURCE.getMetadata().getColumnByName(leftTable.getName(), USERS_ID);

        final DatasourceColumn leftDataColumn = LEFT_DATASOURCE.getMetadata().getColumnByName(leftTable.getName(), columnName);
        final DatasourceColumn rightDataColumn = RIGHT_DATASOURCE.getMetadata().getColumnByName(rightTable.getName(), columnName);

        final TablePair tablePair = TablePair.builder()
                .id(UUID_GENERATOR.generateRandomUuid())
                .name(leftTable.getName())
                .leftDatasourceTable(leftTable)
                .rightDatasourceTable(rightTable)
                .build();

        final ColumnPair keyColumnPair = ColumnPair.builder()
                .id(UUID_GENERATOR.generateRandomUuid())
                .name(leftKeyColumn.getName())
                .tablePair(tablePair)
                .leftDatasourceColumn(leftKeyColumn)
                .rightDatasourceColumn(rightKeyColumn)
                .leftTransformer(new ObjectToIntegerTransformer())
                .rightTransformer(new ObjectToIntegerTransformer())
                .build();

        final ColumnPair dataColumnPair = ColumnPair.builder()
                .id(UUID_GENERATOR.generateRandomUuid())
                .name(columnName)
                .tablePair(tablePair)
                .leftDatasourceColumn(leftDataColumn)
                .rightDatasourceColumn(rightDataColumn)
                .leftTransformer(new ObjectToStringTransformer())
                .rightTransformer(new ObjectToStringTransformer())
                .build();

        tablePair.setKeyColumnPair(keyColumnPair);

        final ColumnData<String, ?> data = storage.getData(
                Query.builder()
                        .tablePair(tablePair)
                        .columnPair(dataColumnPair)
                        .build());

        assertThat(data).isNotNull();
        assertThat(data.getKeyColumn()).isEqualTo(leftKeyColumn);
        assertThat(data.getDataColumn()).isEqualTo(leftDataColumn);
        assertThat(data.getKeys().stream().sorted().collect(Collectors.toList())).isEqualTo(IDS);

        final List<Object> values = new ArrayList<>();
        for (String key : IDS) {
            values.add(data.getValueByKey(key));
        }
        assertThat(values).isEqualTo(expectedValues);
    }

    @Test
    void getColumnDataInfoTest() {
        final DefaultDataStorage storage = new DefaultDataStorage(
                DataStorageConfig.builder()
                        .relationType(LEFT)
                        .maxConnections(1)
                        .build(),
                LEFT_DATASOURCE,
                new InMemoryColumnDataCache());

        final Query query = Query.builder()
                .tablePair(USERS_TABLE_PAIR)
                .columnPair(USERS_USERNAME_COLUMN_PAIR)
                .build();

        final ColumnDataInfo columnDataInfo = storage.getColumnDataInfo(query);
        assertThat(columnDataInfo).isNotNull();
        assertThat(columnDataInfo.isCached()).isFalse();
        assertThat(columnDataInfo.getDate()).isNull();

        storage.getData(query);

        final ColumnDataInfo columnDataInfoAfterCaching = storage.getColumnDataInfo(query);
        assertThat(columnDataInfoAfterCaching).isNotNull();
        assertThat(columnDataInfoAfterCaching.isCached()).isTrue();
        assertThat(columnDataInfoAfterCaching.getDate()).isNotNull();
    }

    @Test
    void deleteCacheTest() {
        final InMemoryColumnDataCache cacheMock = Mockito.spy(InMemoryColumnDataCache.class);
        final DefaultDataStorage storage = new DefaultDataStorage(
                DataStorageConfig.builder()
                        .relationType(LEFT)
                        .maxConnections(1)
                        .build(),
                LEFT_DATASOURCE,
                cacheMock);

        final Query usernameQuery = Query.builder()
                .tablePair(USERS_TABLE_PAIR)
                .columnPair(USERS_USERNAME_COLUMN_PAIR)
                .build();
        final Query passwordQuery = Query.builder()
                .tablePair(USERS_TABLE_PAIR)
                .columnPair(USERS_PASSWORD_COLUMN_PAIR)
                .build();

        storage.getData(usernameQuery);
        storage.getData(passwordQuery);

        assertThat(storage.getColumnDataInfo(usernameQuery).isCached()).isTrue();
        assertThat(storage.getColumnDataInfo(passwordQuery).isCached()).isTrue();

        storage.deleteCache();

        assertThat(storage.getColumnDataInfo(usernameQuery).isCached()).isFalse();
        assertThat(storage.getColumnDataInfo(passwordQuery).isCached()).isFalse();

        Mockito.verify(cacheMock, times(1)).cleanUp();
    }

    @Test
    void deleteColumnCacheTest() {
        final DefaultDataStorage storage = new DefaultDataStorage(
                DataStorageConfig.builder()
                        .relationType(LEFT)
                        .maxConnections(1)
                        .build(),
                LEFT_DATASOURCE,
                new InMemoryColumnDataCache());

        final Query query = Query.builder()
                .tablePair(USERS_TABLE_PAIR)
                .columnPair(USERS_USERNAME_COLUMN_PAIR)
                .build();

        storage.getData(query);
        assertThat(storage.getColumnDataInfo(query).isCached()).isTrue();

        storage.deleteCache(query);
        assertThat(storage.getColumnDataInfo(query).isCached()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void incorrectInputConstructorTest(DataStorageConfig config, Datasource datasource, ColumnDataCache cache) {
        assertThatThrownBy(() -> new DefaultDataStorage(config, datasource, cache))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
