package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.cache.ColumnDataCache;
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
    private final ColumnDataCache cache;
    private final PriorityThreadPoolExecutor executor;

    @Builder
    public DefaultDataStorage(DataStorageConfig config, ColumnDataCache cache) {
        this.config = config;
        this.cache = cache;
        this.executor = new PriorityThreadPoolExecutor(config.getMaxConnections(), config.getMaxConnections(), 0L, TimeUnit.MILLISECONDS);
    }

    @Override
    public DataStorageConfig getConfig() {
        return this.config;
    }

    @Override
    public ColumnDataCache getCache() {
        return null;
    }

    @Override
    @SneakyThrows
    public <K, V> ColumnData<K, V> getData(Query query) {
        return cache.getOrLoad(
                query.getColumnPair().getColumnFor(config.getRelationType()),
                () -> {
                    try {
                        return (ColumnData<K, V>) executor.submit(
                                LoadDataJob.builder()
                                        .columnDataCache(cache)
                                        .datasource(config.getDatasource())
                                        .query(toDatasourceQuery(config.getDatasource(), config.getRelationType(), query))
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
        if (!cache.exist(query.getColumnPair().getColumnFor(config.getRelationType()))) {
            executor.submit(() ->
                            CacheDataJob.builder()
                                    .datasource(config.getDatasource())
                                    .columnDataCache(cache)
                                    .query(toDatasourceQuery(config.getDatasource(), config.getRelationType(), query))
                                    .build(),
                    Priority.LOW);
        }
    }

    @Override
    public void stopPreloadInBackground() {
        executor.remove(Priority.LOW);
    }

    @Override
    public void deleteCache(Query query) {
        cache.delete(query.getColumnPair().getColumnFor(config.getRelationType()));
    }

    private DatasourceQuery toDatasourceQuery(Datasource datasource, RelationType relationType, Query query) {
        return DatasourceQuery.builder()
                .table(query.getTablePair().getTableFor(relationType))
                .primaryKey(
                        datasource.getMetadata().getColumnByName(
                                query.getTablePair().getTableFor(relationType).getName(),
                                query.getTablePair().getTableFor(relationType).getPrimaryKey())
                                .get())
                .column(query.getColumnPair().getColumnFor(relationType))
                .queryParams(query.getQueryParams())
                .build();
    }
}
