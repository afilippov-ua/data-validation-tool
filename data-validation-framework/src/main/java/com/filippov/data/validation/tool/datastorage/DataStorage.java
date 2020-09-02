package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.model.CachingStatus;
import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.model.ColumnDataInfo;

public interface DataStorage {

    DataStorageConfig getConfig();

    Datasource getDatasource();

    <K, V> ColumnData<K, V> getData(Query query);

    ColumnDataInfo getColumnDataInfo(Query query);

    void preloadInBackground(Query query);

    void stopPreloadInBackground(Query query);

    CachingStatus getCachingStatus(Query query);

    void deleteCache(Query query);
}
