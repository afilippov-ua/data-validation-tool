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

import com.filippov.data.validation.tool.model.ColumnDataCache;
import com.filippov.data.validation.tool.model.cache.CacheConfig;
import com.filippov.data.validation.tool.model.datasource.ColumnData;
import com.filippov.data.validation.tool.model.cache.CacheInfo;
import com.filippov.data.validation.tool.model.datasource.DatasourceColumn;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class InMemoryColumnDataCache implements ColumnDataCache {

    private final Map<DatasourceColumn, ColumnData<?, ?>> dataMap;
    private final Map<DatasourceColumn, Instant> cacheDates;

    public InMemoryColumnDataCache(CacheConfig cacheConfig) {
        this.dataMap = new LinkedHashMap<>() {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                return size() > cacheConfig.getMaxNumberOfElementsInCache();
            }
        };
        this.cacheDates = new LinkedHashMap<>() {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                return size() > cacheConfig.getMaxNumberOfElementsInCache();
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Optional<ColumnData<K, V>> get(DatasourceColumn column) {
        log.debug("Loading data from in-memory column data cache for column: {}", column);
        return Optional.ofNullable((ColumnData<K, V>) dataMap.get(column));
    }

    @Override
    public <K, V> void put(DatasourceColumn column, ColumnData<K, V> columnData) {
        log.debug("Putting column data to in-memory column data cache for column: {}", column);
        dataMap.put(column, columnData);
        cacheDates.put(column, Instant.now());
    }

    @Override
    public boolean exist(DatasourceColumn column) {
        return dataMap.containsKey(column);
    }

    @Override
    public void delete(DatasourceColumn column) {
        log.debug("Deleting data from in-memory column data cache for column: {}", column);
        dataMap.remove(column);
        cacheDates.remove(column);
        log.debug("Data from in-memory column data cache has been successfully deleted for column: {}", column);
    }

    @Override
    public void deleteAll() {
        dataMap.clear();
    }

    @Override
    public CacheInfo getColumnCacheDetails(DatasourceColumn column) {
        log.debug("Getting column cache details from in-memory column data cache for column: {}", column);
        return CacheInfo.builder()
                .cached(dataMap.containsKey(column))
                .date(cacheDates.get(column))
                .build();
    }
}