package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.datastorage.cache.ColumnDataCache;
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
    private final RelationType relationType;
    private final Datasource datasource;
    private final ColumnDataCache cache;
    private final PriorityThreadPoolExecutor executor;

    @Builder
    public DefaultDataStorage(RelationType relationType, Datasource datasource, ColumnDataCache cache, Integer maxThreads) {
        this.relationType = relationType;
        this.datasource = datasource;
        this.cache = cache;
        this.executor = new PriorityThreadPoolExecutor(maxThreads, maxThreads, 0L, TimeUnit.MILLISECONDS);
    }

    @Override
    public RelationType getRelationType() {
        return relationType;
    }

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    @SneakyThrows
    public <K, V> ColumnData<K, V> getData(Query query) {
        return cache.getOrLoad(
                query.getColumnPair().getColumnFor(relationType),
                () -> {
                    try {
                        return (ColumnData<K, V>) executor.submit(
                                LoadDataJob.builder()
                                        .columnDataCache(cache)
                                        .datasource(datasource)
                                        .query(toDatasourceQuery(datasource, relationType, query))
                                        .build(),
                                Priority.HIGH)
                                .get();
                    } catch (ExecutionException | InterruptedException e) {
                        log.error("Loading data from {} datasource has been failed", relationType, e);
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void preloadInBackground(Query query) {
        if (!cache.exist(query.getColumnPair().getColumnFor(relationType))) {
            executor.submit(() ->
                            CacheDataJob.builder()
                                    .datasource(datasource)
                                    .columnDataCache(cache)
                                    .query(toDatasourceQuery(datasource, relationType, query))
                                    .build(),
                    Priority.LOW);
        }
    }

    @Override
    public void stopBackgroundPreloading() {
        executor.remove(Priority.LOW);
    }

    @Override
    public void deleteCache(Query query) {
        cache.delete(query.getColumnPair().getColumnFor(relationType));
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
