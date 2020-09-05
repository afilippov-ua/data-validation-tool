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

import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@ToString(of = {"keyColumn", "dataColumn"})
@EqualsAndHashCode(of = {"keyColumn", "dataColumn"})
public class ColumnData<K, V> implements Serializable {

    private final DatasourceColumn keyColumn;
    private final DatasourceColumn dataColumn;
    private final Map<K, V> dataMap;

    @Builder
    public ColumnData(DatasourceColumn keyColumn, DatasourceColumn dataColumn, List<K> keys, List<V> data) {
        if (keys.size() != data.size()) {
            throw new RuntimeException("Datasource returned different number of keys and values for key column: " + keyColumn + " and data column: " + dataColumn);
        }

        this.keyColumn = keyColumn;
        this.dataColumn = dataColumn;
        this.dataMap = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            dataMap.put(keys.get(i), data.get(i));
        }
    }

    public ColumnData(DatasourceColumn keyColumn, DatasourceColumn dataColumn, Map<K, V> dataMap) {
        this.keyColumn = keyColumn;
        this.dataColumn = dataColumn;
        this.dataMap = dataMap;
    }

    public V getValueByKey(K key) {
        return dataMap.get(key);
    }

    public boolean containsKey(K key) {
        return dataMap.containsKey(key);
    }

    public Set<K> getKeys() {
        return dataMap.keySet();
    }
}