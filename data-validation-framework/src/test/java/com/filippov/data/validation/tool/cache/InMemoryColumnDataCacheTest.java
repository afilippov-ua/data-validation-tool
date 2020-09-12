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

package com.filippov.data.validation.tool.cache;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.model.ColumnDataInfo;
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

    private static final List<Integer> IDS = asList(1, 2, 3, 4, 5, 6, 7);
    private static final List<String> VALUES = asList("str1", "str2", "str3", "str4", "str5", "str6", "str7");
    private static final ColumnData<Integer, ?> TEST_DATA = ColumnData.<Integer, String>builder()
            .keyColumn(USERS_ID_COLUMN)
            .dataColumn(USERS_USERNAME_COLUMN)
            .keys(IDS)
            .data(VALUES)
            .build();
    private static final Map<Integer, String> DATA_MAP;

    static {
        DATA_MAP = new HashMap<>();
        for (int i = 0; i < IDS.size(); i++) {
            DATA_MAP.put(IDS.get(i), VALUES.get(i));
        }
    }

    @Test
    void getOrLoadExceptionTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache();
        cache.delete(USERS_USERNAME_COLUMN);
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isFalse();

        assertThrows(RuntimeException.class, () ->
                cache.getOrLoad(USERS_USERNAME_COLUMN, () -> {
                    throw new RuntimeException("test exception");
                }));

        cache.cleanUp();
        cache.close();
    }

    @Test
    void getOrLoadTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache();
        cache.delete(USERS_USERNAME_COLUMN);
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isFalse();

        ColumnData<Integer, ?> data = cache.getOrLoad(USERS_USERNAME_COLUMN, () -> TEST_DATA);
        assertThat(data).isNotNull();
        assertThat(data.getKeyColumn()).isEqualTo(USERS_ID_COLUMN);
        assertThat(data.getDataColumn()).isEqualTo(USERS_USERNAME_COLUMN);
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
        cache.put(USERS_USERNAME_COLUMN, TEST_DATA);

        final Optional<ColumnData<Integer, String>> optionalData = cache.get(USERS_USERNAME_COLUMN);
        assertThat(optionalData).isNotEmpty();
        optionalData.ifPresent(data -> {
            assertThat(data.getKeyColumn()).isEqualTo(USERS_ID_COLUMN);
            assertThat(data.getDataColumn()).isEqualTo(USERS_USERNAME_COLUMN);
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
        cache.delete(USERS_USERNAME_COLUMN);
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isFalse();

        cache.putIfNotExist(USERS_USERNAME_COLUMN, () -> TEST_DATA);

        final Optional<ColumnData<Integer, String>> optionalData = cache.get(USERS_USERNAME_COLUMN);
        assertThat(optionalData).isNotEmpty();
        optionalData.ifPresent(data -> {
            assertThat(data.getKeyColumn()).isEqualTo(USERS_ID_COLUMN);
            assertThat(data.getDataColumn()).isEqualTo(USERS_USERNAME_COLUMN);
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
        cache.delete(USERS_USERNAME_COLUMN);
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isFalse();
        cache.put(USERS_USERNAME_COLUMN, TEST_DATA);
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isTrue();

        cache.cleanUp();
        cache.close();
    }

    @Test
    void deleteCacheTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache();
        if (!cache.exist(USERS_USERNAME_COLUMN)) {
            cache.put(USERS_USERNAME_COLUMN, TEST_DATA);
        }
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isTrue();
        cache.delete(USERS_USERNAME_COLUMN);
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isFalse();
        assertThat(cache.get(USERS_USERNAME_COLUMN)).isEmpty();

        cache.cleanUp();
        cache.close();
    }

    @Test
    void getColumnCacheDetailsTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache();
        if (!cache.exist(USERS_USERNAME_COLUMN)) {
            cache.put(USERS_USERNAME_COLUMN, TEST_DATA);
        }

        final ColumnDataInfo columnCacheDetails = cache.getColumnCacheDetails(USERS_USERNAME_COLUMN);
        assertThat(columnCacheDetails).isNotNull();
        assertThat(columnCacheDetails.isCached()).isTrue();
        assertThat(columnCacheDetails.getDate()).isNotNull();

        cache.cleanUp();
        cache.close();
    }
}
