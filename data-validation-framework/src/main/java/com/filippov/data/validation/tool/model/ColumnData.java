package com.filippov.data.validation.tool.model;

import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@EqualsAndHashCode(of = {"keyColumn", "dataColumn"})
public class ColumnData<K, V> implements Serializable {

    private DatasourceColumn keyColumn;
    private DatasourceColumn dataColumn;
    private Map<K, V> dataMap;

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