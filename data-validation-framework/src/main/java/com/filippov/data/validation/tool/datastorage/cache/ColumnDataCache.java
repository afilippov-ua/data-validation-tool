package com.filippov.data.validation.tool.datastorage.cache;

import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.model.ColumnData;

import java.util.function.Supplier;

public interface ColumnDataCache {

    ColumnData get(DatasourceColumn column);

    ColumnData getOrLoad(DatasourceColumn column, Supplier<ColumnData> supplier);

    void put(DatasourceColumn column, ColumnData columnData);

    void putIfNotExist(DatasourceColumn column, Supplier<ColumnData> supplier);

    boolean exist(DatasourceColumn column);

    void delete(DatasourceColumn column);
}
