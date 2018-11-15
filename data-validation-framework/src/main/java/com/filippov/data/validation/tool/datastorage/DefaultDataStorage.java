package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.model.ColumnData;

public class DefaultDataStorage implements DataStorage {

    @Override
    public RelationType getRelationType() {
        return null;
    }

    @Override
    public Datasource getDatasource() {
        return null;
    }

    @Override
    public ColumnData getData(Query query) {
        return null;
    }

    @Override
    public void preloadAsync(Query query) {

    }

    @Override
    public void stopAsyncPreloading(Query query) {

    }

    @Override
    public void deleteCache(Query query) {

    }
}
