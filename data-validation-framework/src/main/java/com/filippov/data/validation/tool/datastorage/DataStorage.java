package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.model.ColumnData;

public interface DataStorage {

    DataStorageConfig getConfig();

    ColumnDataCache getCache();

    <K, V> ColumnData<K, V> getData(Query query);

    void preloadInBackground(Query query);

    void stopPreloadInBackground();

    void deleteCache(Query query);
}
