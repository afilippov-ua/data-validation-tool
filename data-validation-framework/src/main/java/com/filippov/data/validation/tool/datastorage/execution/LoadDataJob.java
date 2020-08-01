package com.filippov.data.validation.tool.datastorage.execution;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.model.ColumnData;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class LoadDataJob<K, V> implements Callable<ColumnData<K, V>> {
    private ColumnDataCache columnDataCache;
    private Datasource datasource;
    private DatasourceQuery query;

    @Builder
    public LoadDataJob(ColumnDataCache columnDataCache, Datasource datasource, DatasourceQuery query) {
        this.columnDataCache = columnDataCache;
        this.datasource = datasource;
        this.query = query;
    }

    @Override
    public ColumnData<K, V> call() throws Exception {
        return columnDataCache.getOrLoad(query.getDataColumn(), () -> {
            try {
                return datasource.getColumnData(query);
            } catch (Exception e) {
                log.error("Loading data from datasource has been failed", e);
                throw e;
            }
        });
    }
}
