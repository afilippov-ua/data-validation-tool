package com.filippov.data.validation.tool.datastorage.execution;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.datastorage.cache.ColumnDataCache;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheDataJob implements Runnable {
    private ColumnDataCache columnDataCache;
    private Datasource datasource;
    private DatasourceQuery query;

    @Builder
    public CacheDataJob(ColumnDataCache columnDataCache, Datasource datasource, DatasourceQuery query) {
        this.columnDataCache = columnDataCache;
        this.datasource = datasource;
        this.query = query;
    }

    @Override
    public void run() {
        columnDataCache.putIfNotExist(query.getColumn(), () -> {
            try {
                return datasource.getColumnData(query);
            } catch (Exception e) {
                log.error("Loading data from datasource has been failed", e);
                throw new RuntimeException(e);
            }
        });
    }
}
