package com.filippov.data.validation.tool.datastorage.execution.job;

import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.model.ColumnData;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(of = {"datasource", "query"})
public class CacheDataJob implements Runnable {
    private final ColumnDataCache columnDataCache;
    private final Datasource datasource;
    private final DatasourceQuery query;

    @Builder
    public CacheDataJob(ColumnDataCache columnDataCache, Datasource datasource, DatasourceQuery query) {
        this.columnDataCache = columnDataCache;
        this.datasource = datasource;
        this.query = query;
    }

    @Override
    public void run() {
        log.debug("Caching data job has been started for datasource: {} and query: {}", datasource, query);
        columnDataCache.putIfNotExist(query.getDataColumn(), () -> {
            try {
                final ColumnData<?, ?> result = datasource.getColumnData(query);
                log.debug("Caching data job has been finished for datasource: {} and query: {}", datasource, query);
                return result;
            } catch (Exception e) {
                throw new RuntimeException("Loading data from datasource has been failed for datasource: " + datasource + " and query: " + query, e);
            }
        });
    }
}
