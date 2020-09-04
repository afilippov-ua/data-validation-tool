package com.filippov.data.validation.tool.factory;

import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.datasource.Datasource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultColumnDataCacheFactory implements ColumnDataCacheFactory {

    @Override
    public ColumnDataCache getOrCreateForDatasource(Datasource datasource) {
        log.debug("Creating column data cache for datasource: {}. Using: InMemoryColumnDataCache", datasource);
        return new InMemoryColumnDataCache(); // TODO: use in-memory only for now
    }
}
