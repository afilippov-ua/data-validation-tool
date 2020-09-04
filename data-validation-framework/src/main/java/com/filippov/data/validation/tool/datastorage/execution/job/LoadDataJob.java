package com.filippov.data.validation.tool.datastorage.execution.job;

import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.model.ColumnData;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
@ToString(of = {"datasource", "query"})
public class LoadDataJob<K, V> implements Callable<ColumnData<K, V>> {
    private final ColumnDataCache columnDataCache;
    private final Datasource datasource;
    private final DatasourceQuery query;

    @Builder
    public LoadDataJob(ColumnDataCache columnDataCache, Datasource datasource, DatasourceQuery query) {
        this.columnDataCache = columnDataCache;
        this.datasource = datasource;
        this.query = query;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ColumnData<K, V> call() throws Exception {
        log.debug("Loading data job has been started for datasource: {} and query: {}", datasource, query);
        return columnDataCache.get(query.getDataColumn())
                .map(data -> (ColumnData<K, V>) data)
                .orElseGet(() -> {
                    try {
                        final ColumnData<K, V> result = datasource.getColumnData(query);
                        log.debug("Caching data job has been finished for datasource: {} and query: {}", datasource, query);
                        return result;
                    } catch (Exception e) {
                        throw new RuntimeException("Loading data from datasource has been failed for datasource: " + datasource + " and query: " + query, e);
                    }
                });
    }
}
