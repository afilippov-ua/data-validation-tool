package com.filippov.data.validation.tool.datastorage.cache;

import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.model.ColumnData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryColumnDataCache implements ColumnDataCache {

    private Map<DatasourceColumn, ColumnData> dataMap = new HashMap<>();

    @Override
    public Optional<ColumnData> get(DatasourceColumn column) {
        return Optional.ofNullable(dataMap.get(column));
    }

    @Override
    public void put(DatasourceColumn column, ColumnData columnData) {
        dataMap.put(column, columnData);
    }

    @Override
    public boolean exist(DatasourceColumn column) {
        return dataMap.containsKey(column);
    }

    @Override
    public void delete(DatasourceColumn column) {
        dataMap.remove(column);
    }

    @Override
    public void flush() {

    }

    @Override
    public void cleanUp() {
        dataMap.clear();
    }

    @Override
    public void close() {

    }
}
