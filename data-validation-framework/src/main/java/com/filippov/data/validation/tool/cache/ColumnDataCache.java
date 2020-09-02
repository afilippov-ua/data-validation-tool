package com.filippov.data.validation.tool.cache;

import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.model.ColumnDataInfo;

import java.util.Optional;
import java.util.function.Supplier;

public interface ColumnDataCache {

    <K, V> Optional<ColumnData<K, V>> get(DatasourceColumn column);

    default <K, V> ColumnData<K, V> getOrLoad(DatasourceColumn column, Supplier<ColumnData<K, V>> supplier) {
        if (exist(column)) {
            return (ColumnData<K, V>) get(column).get();
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

    boolean exist(DatasourceColumn column);

    void delete(DatasourceColumn column);

    void flush();

    void cleanUp();

    void close();

    ColumnDataInfo getColumnCacheDetails(DatasourceColumn column);

    // TODO:
    // smth like that
    // dvt-instance-id/workspace-id/left/table-name/column-name/
    //                             /right/table-name/column-name/
}
