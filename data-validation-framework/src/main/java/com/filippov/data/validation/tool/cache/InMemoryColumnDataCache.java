package com.filippov.data.validation.tool.cache;

import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.model.ColumnDataInfo;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryColumnDataCache implements ColumnDataCache {

    private Map<DatasourceColumn, ColumnData<?, ?>> dataMap = new HashMap<>();
    private Map<DatasourceColumn, Instant> cachingDates = new HashMap<>();

    @Override
    public <K, V> Optional<ColumnData<K, V>> get(DatasourceColumn column) {
        return Optional.ofNullable((ColumnData<K, V>) dataMap.get(column));
    }

    @Override
    public <K, V> void put(DatasourceColumn column, ColumnData<K, V> columnData) {
        dataMap.put(column, columnData);
        cachingDates.put(column, Instant.now());
    }

    @Override
    public boolean exist(DatasourceColumn column) {
        return dataMap.containsKey(column);
    }

    @Override
    public void delete(DatasourceColumn column) {
        dataMap.remove(column);
        cachingDates.remove(column);
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

    @Override
    public ColumnDataInfo getColumnCacheDetails(DatasourceColumn column) {
        return ColumnDataInfo.builder()
                .cached(dataMap.containsKey(column))
                .date(cachingDates.get(column))
                .build();
    }
}
