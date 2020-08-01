package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.datastorage.execution.CacheDataJob;
import com.filippov.data.validation.tool.datastorage.execution.LoadDataJob;
import com.filippov.data.validation.tool.executor.Priority;
import com.filippov.data.validation.tool.executor.PriorityThreadPoolExecutor;
import com.filippov.data.validation.tool.model.ColumnData;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DefaultDataStorage implements DataStorage {
    private final DataStorageConfig config;
    private final Datasource datasource;
    private final ColumnDataCache cache;
    private final PriorityThreadPoolExecutor executor;

    @Builder
    public DefaultDataStorage(DataStorageConfig config, Datasource datasource, ColumnDataCache cache) {
        this.config = config;
        this.datasource = datasource;
        this.cache = cache;
        this.executor = new PriorityThreadPoolExecutor(config.getMaxConnections(), config.getMaxConnections(), 0L, TimeUnit.MILLISECONDS);
    }

    @Override
    public DataStorageConfig getConfig() {
        return this.config;
    }

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <K, V> ColumnData<K, V> getData(Query query) {
        final DatasourceQuery datasourceQuery = datasource.toDatasourceQuery(query, config.getRelationType());
        return cache.getOrLoad(
                datasourceQuery.getDataColumn(),
                () -> {
                    try {
                        return (ColumnData<K, V>) executor.submit(
                                LoadDataJob.builder()
                                        .columnDataCache(cache)
                                        .datasource(datasource)
                                        .query(datasourceQuery)
                                        .build(),
                                Priority.HIGH)
                                .get();
                    } catch (ExecutionException | InterruptedException e) {
                        log.error("Loading data from {} datasource has been failed", config.getRelationType(), e);
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void preloadInBackground(Query query) {
        final DatasourceQuery datasourceQuery = datasource.toDatasourceQuery(query, config.getRelationType());
        if (cache.exist(datasourceQuery.getDataColumn())) {
            log.debug("Cache for table: " + query.getTablePair().getName() + ", column: "
                    + query.getColumnPair().getName() + " is already exist! Skip.");
        } else {
            executor.submit(() ->
                            CacheDataJob.builder()
                                    .datasource(datasource)
                                    .columnDataCache(cache)
                                    .query(datasourceQuery)
                                    .build(),
                    Priority.LOW);
        }
    }

    @Override
    public void stopPreloadInBackground() {
        executor.remove(Priority.LOW); // TODO !!! this will remove all caching tasks
    }

    @Override
    public void deleteCache(Query query) {
        cache.delete(query.getColumnPair().getColumnFor(config.getRelationType()));
    }
}
