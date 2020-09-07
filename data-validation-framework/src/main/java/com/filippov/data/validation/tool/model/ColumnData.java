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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ToString(of = {"keyColumn", "dataColumn"})
@EqualsAndHashCode(of = {"keyColumn", "dataColumn"})
public class ColumnData<K, V> implements Serializable {

    @Getter
    private final DatasourceColumn keyColumn;
    @Getter
    private final DatasourceColumn dataColumn;
    @Getter
    private final Map<K, V> dataMap;

    @JsonCreator
    public ColumnData(@JsonProperty("keyColumn") DatasourceColumn keyColumn,
                      @JsonProperty("dataColumn") DatasourceColumn dataColumn,
                      @JsonProperty("dataMap") Map<K, V> dataMap) {
        this.keyColumn = keyColumn;
        this.dataColumn = dataColumn;
        this.dataMap = dataMap;
    }

    public static <K, V> ColumnDataBuilder<K, V> builder() {
        return new ColumnDataBuilder<K, V>();
    }

    @JsonIgnore
    public V getValueByKey(K key) {
        return dataMap.get(key);
    }

    @JsonIgnore
    public boolean containsKey(K key) {
        return dataMap.containsKey(key);
    }

    @JsonIgnore
    public Set<K> getKeys() {
        return dataMap.keySet();
    }

    public static class ColumnDataBuilder<K, V> {
        private DatasourceColumn keyColumn;
        private DatasourceColumn dataColumn;
        private List<K> keys;
        private List<V> data;

        ColumnDataBuilder() {
        }

        public ColumnDataBuilder<K, V> keyColumn(DatasourceColumn keyColumn) {
            this.keyColumn = keyColumn;
            return this;
        }

        public ColumnDataBuilder<K, V> dataColumn(DatasourceColumn dataColumn) {
            this.dataColumn = dataColumn;
            return this;
        }

        public ColumnDataBuilder<K, V> keys(List<K> keys) {
            this.keys = keys;
            return this;
        }

        public ColumnDataBuilder<K, V> data(List<V> data) {
            this.data = data;
            return this;
        }

        public ColumnData<K, V> build() {
            if (keys.size() != data.size()) {
                throw new RuntimeException("Datasource returned different number of keys and values for key column: " + keyColumn + " and data column: " + dataColumn);
            }
            final Map<K, V> dataMap = new HashMap<>();
            for (int i = 0; i < keys.size(); i++) {
                dataMap.put(keys.get(i), data.get(i));
            }
            return new ColumnData<K, V>(keyColumn, dataColumn, dataMap);
        }
    }
}