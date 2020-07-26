package com.filippov.data.validation.tool.factory;

import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.datasource.Datasource;

public interface ColumnDataCacheFactory {
    ColumnDataCache getOrCreateForDatasource(Datasource datasource);
}
