package com.filippov.data.validation.tool.datastorage.cache;

import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.model.ColumnData;

import java.util.function.Supplier;

public class DiskColumnDataCache implements ColumnDataCache {

    @Override
    public ColumnData get(DatasourceColumn column) {
        return null;
    }

    @Override
    public ColumnData getOrLoad(DatasourceColumn column, Supplier<ColumnData> supplier) {
        return null;
    }

    @Override
    public void put(DatasourceColumn column, ColumnData columnData) {

    }

    @Override
    public void putIfNotExist(DatasourceColumn column, Supplier<ColumnData> supplier) {

    }

    @Override
    public boolean exist(DatasourceColumn column) {
        return false;
    }

    @Override
    public void delete(DatasourceColumn column) {

    }
}
