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

package com.filippov.data.validation.tool.datastorage.cache;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.model.ColumnData;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryColumnDataCacheTest extends AbstractTest {
    private static final String TEST_TABLE_NAME = "test-table-name";
    private static final String PK_COLUMN_NAME = "pk";
    private static final String TEST_COLUMN_NAME = "test-column-name";

    private static final DatasourceTable TABLE;
    private static final DatasourceColumn PK;
    private static final DatasourceColumn TEST_COLUMN;
    private static final List<Integer> IDS;
    private static final List<String> VALUES;
    private static final Map<Integer, String> DATA_MAP;
    private static final ColumnData<Integer, ?> TEST_DATA;

    static {
        PK = DatasourceColumn.builder().name(PK_COLUMN_NAME).tableName(TEST_TABLE_NAME).build();
        TEST_COLUMN = DatasourceColumn.builder().name(TEST_COLUMN_NAME).tableName(TEST_TABLE_NAME).build();

        TABLE = DatasourceTable.builder()
                .primaryKey(PK.getName())
                .columns(asList(PK.getName(), TEST_COLUMN.getName()))
                .name(TEST_TABLE_NAME)
                .build();

        IDS = asList(1, 2, 3, 4, 5, 6, 7);
        VALUES = asList("str1", "str2", "str3", "str4", "str5", "str6", "str7");
        TEST_DATA = ColumnData.<Integer, String>builder().keyColumn(PK).dataColumn(TEST_COLUMN).keys(IDS).data(VALUES).build();

        DATA_MAP = new HashMap<>();
        for (int i = 0; i < IDS.size(); i++) {
            DATA_MAP.put(IDS.get(i), VALUES.get(i));
        }
    }

    @Test
    void getOrLoadExceptionTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache();
        cache.delete(TEST_COLUMN);
        assertThat(cache.exist(TEST_COLUMN)).isFalse();

        assertThrows(RuntimeException.class, () ->
                cache.getOrLoad(TEST_COLUMN, () -> {
                    throw new RuntimeException("test exception");
                }));

        cache.cleanUp();
        cache.close();
    }

    @Test
    void getOrLoadTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache();
        cache.delete(TEST_COLUMN);
        assertThat(cache.exist(TEST_COLUMN)).isFalse();

        ColumnData<Integer, ?> data = cache.getOrLoad(TEST_COLUMN, () -> TEST_DATA);
        assertThat(data).isNotNull();
        assertThat(data.getKeyColumn()).isEqualTo(PK);
        assertThat(data.getDataColumn()).isEqualTo(TEST_COLUMN);
        assertThat(data.getKeys().stream().sorted().collect(toList())).isEqualTo(IDS);
        for (int id : IDS) {
            assertThat(data.getValueByKey(id)).isEqualTo(DATA_MAP.get(id));
        }
        cache.cleanUp();
        cache.close();
    }

    @Test
    void putAndGetTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache();
        cache.put(TEST_COLUMN, TEST_DATA);

        final Optional<ColumnData<Integer, String>> optionalData = cache.get(TEST_COLUMN);
        assertThat(optionalData).isNotEmpty();
        optionalData.ifPresent(data -> {
            assertThat(data.getKeyColumn()).isEqualTo(PK);
            assertThat(data.getDataColumn()).isEqualTo(TEST_COLUMN);
            assertThat(data.getKeys().stream().sorted().collect(toList())).isEqualTo(IDS);
            for (int id : IDS) {
                assertThat(data.getValueByKey(id)).isEqualTo(DATA_MAP.get(id));
            }
        });

        cache.cleanUp();
        cache.close();
    }

    @Test
    void putIfNotExistAndGetTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache();
        cache.delete(TEST_COLUMN);
        assertThat(cache.exist(TEST_COLUMN)).isFalse();

        cache.putIfNotExist(TEST_COLUMN, () -> TEST_DATA);

        final Optional<ColumnData<Integer, String>> optionalData = cache.get(TEST_COLUMN);
        assertThat(optionalData).isNotEmpty();
        optionalData.ifPresent(data -> {
            assertThat(data.getKeyColumn()).isEqualTo(PK);
            assertThat(data.getDataColumn()).isEqualTo(TEST_COLUMN);
            assertThat(data.getKeys().stream().sorted().collect(toList())).isEqualTo(IDS);
            for (int id : IDS) {
                assertThat(data.getValueByKey(id)).isEqualTo(DATA_MAP.get(id));
            }
        });

        cache.cleanUp();
        cache.close();
    }

    @Test
    void isCacheExistTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache();
        cache.delete(TEST_COLUMN);
        assertThat(cache.exist(TEST_COLUMN)).isFalse();
        cache.put(TEST_COLUMN, TEST_DATA);
        assertThat(cache.exist(TEST_COLUMN)).isTrue();

        cache.cleanUp();
        cache.close();
    }

    @Test
    void deleteCacheTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache();
        if (!cache.exist(TEST_COLUMN)) {
            cache.put(TEST_COLUMN, TEST_DATA);
        }
        assertThat(cache.exist(TEST_COLUMN)).isTrue();
        cache.delete(TEST_COLUMN);
        assertThat(cache.exist(TEST_COLUMN)).isFalse();
        assertThat(cache.get(TEST_COLUMN)).isEmpty();

        cache.flush();
        cache.cleanUp();
        cache.close();
    }
}
