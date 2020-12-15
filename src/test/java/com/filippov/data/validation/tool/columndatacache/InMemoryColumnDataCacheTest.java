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

package com.filippov.data.validation.tool.columndatacache;

import com.filippov.data.validation.tool.AbstractUnitTest;
import com.filippov.data.validation.tool.model.ColumnDataCache;
import com.filippov.data.validation.tool.model.cache.CacheConfig;
import com.filippov.data.validation.tool.model.cache.CacheInfo;
import com.filippov.data.validation.tool.model.cache.EvictionStrategy;
import com.filippov.data.validation.tool.model.datasource.ColumnData;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryColumnDataCacheTest extends AbstractUnitTest {
    private static final CacheConfig DEFAULT_CACHE_CONFIG = CacheConfig.builder()
            .evictionStrategy(EvictionStrategy.FIFO)
            .maxNumberOfElementsInCache(200)
            .build();
    private static final List<Integer> IDS = asList(1, 2, 3, 4, 5, 6, 7);
    private static final List<String> VALUES = asList("str1", "str2", "str3", "str4", "str5", "str6", "str7");

    private static final ColumnData<Integer, ?> USERS_USERNAME_DATA = ColumnData.<Integer, String>builder()
            .keyColumn(USERS_ID_COLUMN)
            .dataColumn(USERS_USERNAME_COLUMN)
            .keys(IDS)
            .data(VALUES)
            .build();

    private static final ColumnData<Integer, ?> USERS_PASSWORD_DATA = ColumnData.<Integer, String>builder()
            .keyColumn(USERS_ID_COLUMN)
            .dataColumn(USERS_PASSWORD_COLUMN)
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
        final ColumnDataCache cache = new InMemoryColumnDataCache(DEFAULT_CACHE_CONFIG);
        cache.delete(USERS_USERNAME_COLUMN);
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isFalse();

        assertThrows(RuntimeException.class, () ->
                cache.getOrLoad(USERS_USERNAME_COLUMN, () -> {
                    throw new RuntimeException("test exception");
                }));

        cache.deleteAll();
    }

    @Test
    void getOrLoadTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache(DEFAULT_CACHE_CONFIG);
        cache.delete(USERS_USERNAME_COLUMN);
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isFalse();

        ColumnData<Integer, ?> data = cache.getOrLoad(USERS_USERNAME_COLUMN, () -> USERS_USERNAME_DATA);
        assertThat(data).isNotNull();
        assertThat(data.getKeyColumn()).isEqualTo(USERS_ID_COLUMN);
        assertThat(data.getDataColumn()).isEqualTo(USERS_USERNAME_COLUMN);
        assertThat(data.getKeys().stream().sorted().collect(toList())).isEqualTo(IDS);
        for (int id : IDS) {
            assertThat(data.getValueByKey(id)).isEqualTo(DATA_MAP.get(id));
        }
        cache.deleteAll();
    }

    @Test
    void putAndGetTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache(DEFAULT_CACHE_CONFIG);
        cache.put(USERS_USERNAME_COLUMN, USERS_USERNAME_DATA);

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

        cache.deleteAll();
    }

    @Test
    void putIfNotExistAndGetTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache(DEFAULT_CACHE_CONFIG);
        cache.delete(USERS_USERNAME_COLUMN);
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isFalse();

        cache.putIfNotExist(USERS_USERNAME_COLUMN, () -> USERS_USERNAME_DATA);

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

        cache.deleteAll();
    }

    @Test
    void isCacheExistTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache(DEFAULT_CACHE_CONFIG);
        cache.delete(USERS_USERNAME_COLUMN);
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isFalse();
        cache.put(USERS_USERNAME_COLUMN, USERS_USERNAME_DATA);
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isTrue();

        cache.deleteAll();
    }

    @Test
    void deleteCacheTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache(DEFAULT_CACHE_CONFIG);
        if (!cache.exist(USERS_USERNAME_COLUMN)) {
            cache.put(USERS_USERNAME_COLUMN, USERS_USERNAME_DATA);
        }
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isTrue();
        cache.delete(USERS_USERNAME_COLUMN);
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isFalse();
        assertThat(cache.get(USERS_USERNAME_COLUMN)).isEmpty();

        cache.deleteAll();
    }

    @Test
    void getColumnCacheDetailsTest() {
        final ColumnDataCache cache = new InMemoryColumnDataCache(DEFAULT_CACHE_CONFIG);
        if (!cache.exist(USERS_USERNAME_COLUMN)) {
            cache.put(USERS_USERNAME_COLUMN, USERS_USERNAME_DATA);
        }

        final CacheInfo columnCacheDetails = cache.getColumnCacheDetails(USERS_USERNAME_COLUMN);
        assertThat(columnCacheDetails).isNotNull();
        assertThat(columnCacheDetails.isCached()).isTrue();
        assertThat(columnCacheDetails.getDate()).isNotNull();

        cache.deleteAll();
    }

    @Test
    void checkMaxNumberOfElementsInCacheWith1Test() {
        final CacheConfig cacheConfig = CacheConfig.builder()
                .evictionStrategy(EvictionStrategy.FIFO)
                .maxNumberOfElementsInCache(1)
                .build();

        final ColumnDataCache cache = new InMemoryColumnDataCache(cacheConfig);
        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isFalse();
        cache.put(USERS_USERNAME_COLUMN, USERS_USERNAME_DATA);

        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isTrue();
        assertThat(cache.get(USERS_USERNAME_COLUMN)).isNotNull();
        assertThat(cache.getColumnCacheDetails(USERS_USERNAME_COLUMN)).isNotNull();

        cache.put(USERS_PASSWORD_COLUMN, USERS_PASSWORD_DATA);

        assertThat(cache.exist(USERS_USERNAME_COLUMN)).isFalse();
        assertThat(cache.get(USERS_USERNAME_COLUMN)).isEmpty();
        final CacheInfo columnCacheDetails = cache.getColumnCacheDetails(USERS_USERNAME_COLUMN);
        assertThat(columnCacheDetails).isNotNull();
        assertThat(columnCacheDetails.isCached()).isFalse();
        assertThat(columnCacheDetails.getDate()).isNull();

        assertThat(cache.exist(USERS_PASSWORD_COLUMN)).isTrue();
        assertThat(cache.get(USERS_PASSWORD_COLUMN)).isNotNull();
        assertThat(cache.getColumnCacheDetails(USERS_PASSWORD_COLUMN)).isNotNull();
    }
}
