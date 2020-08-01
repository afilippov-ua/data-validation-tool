package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.model.ColumnData;

public interface DataStorage {

    DataStorageConfig getConfig();

    Datasource getDatasource();

    <K, V> ColumnData<K, V> getData(Query query);

    void preloadInBackground(Query query);

    void stopPreloadInBackground();

    void deleteCache(Query query);
}
