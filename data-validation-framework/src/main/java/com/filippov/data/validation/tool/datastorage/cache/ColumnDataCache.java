package com.filippov.data.validation.tool.datastorage.cache;

import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.model.ColumnData;

import java.util.Optional;
import java.util.function.Supplier;

public interface ColumnDataCache {

    Optional<ColumnData> get(DatasourceColumn column);

    default ColumnData getOrLoad(DatasourceColumn column, Supplier<ColumnData> supplier) {
        if (exist(column)) {
            return get(column).get();
        } else {
            final ColumnData data = supplier.get();
            put(column, data);
            return data;
        }
    }

    void put(DatasourceColumn column, ColumnData columnData);

    default void putIfNotExist(DatasourceColumn column, Supplier<ColumnData> supplier) {
        if (!exist(column)) {
            put(column, supplier.get());
        }
    }

    boolean exist(DatasourceColumn column);

    void delete(DatasourceColumn column);

    void flush();

    void cleanUp();

    void close();
}
