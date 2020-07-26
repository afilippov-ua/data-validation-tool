package com.filippov.data.validation.tool.factory;

import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.datasource.Datasource;

public class DefaultColumnDataCacheFactory implements ColumnDataCacheFactory {

    @Override
    public ColumnDataCache getOrCreateForDatasource(Datasource datasource) {
        return new InMemoryColumnDataCache(); // TODO: implement real factory
    }
}
