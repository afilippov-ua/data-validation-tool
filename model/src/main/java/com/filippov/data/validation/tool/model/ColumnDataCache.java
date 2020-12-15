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

package com.filippov.data.validation.tool.model;

import com.filippov.data.validation.tool.model.datasource.ColumnData;
import com.filippov.data.validation.tool.model.cache.CacheInfo;
import com.filippov.data.validation.tool.model.datasource.DatasourceColumn;

import java.util.Optional;
import java.util.function.Supplier;

public interface ColumnDataCache {

    <K, V> Optional<ColumnData<K, V>> get(DatasourceColumn column);

    @SuppressWarnings("unchecked")
    default <K, V> ColumnData<K, V> getOrLoad(DatasourceColumn column, Supplier<ColumnData<K, V>> supplier) {
        if (exist(column)) {
            return (ColumnData<K, V>) get(column)
                    .orElseThrow(() -> new RuntimeException("Failed to load column data for table: "
                            + column.getTableName() + " and  column: " + column.getName()));
        } else {
            final ColumnData<K, V> data = supplier.get();
            put(column, data);
            return data;
        }
    }

    <K, V> void put(DatasourceColumn column, ColumnData<K, V> columnData);

    default <K, V> void putIfNotExist(DatasourceColumn column, Supplier<ColumnData<K, V>> supplier) {
        if (!exist(column)) {
            put(column, supplier.get());
        }
    }

    CacheInfo getColumnCacheDetails(DatasourceColumn column);

    boolean exist(DatasourceColumn column);

    void delete(DatasourceColumn column);

    void deleteAll();
}
