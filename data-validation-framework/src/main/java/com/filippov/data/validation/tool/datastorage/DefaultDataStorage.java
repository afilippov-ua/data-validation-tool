package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.datastorage.execution.job.CacheDataJob;
import com.filippov.data.validation.tool.datastorage.execution.job.LoadDataJob;
import com.filippov.data.validation.tool.datastorage.execution.executor.DataStorageExecutor;
import com.filippov.data.validation.tool.datastorage.execution.Priority;
import com.filippov.data.validation.tool.model.CachingStatus;
import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.model.ColumnDataInfo;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.filippov.data.validation.tool.model.CachingStatus.NON_DEFINED;
import static com.filippov.data.validation.tool.model.CachingStatus.RUNNING;

@Slf4j
public class DefaultDataStorage implements DataStorage {
    private final DataStorageConfig config;
    private final Datasource datasource;
    private final ColumnDataCache cache;
    private final DataStorageExecutor executor;

    @Builder
    public DefaultDataStorage(DataStorageConfig config, Datasource datasource, ColumnDataCache cache) {
        this.config = config;
        this.datasource = datasource;
        this.cache = cache;
        this.executor = new DataStorageExecutor(config.getMaxConnections(), config.getMaxConnections(), 0L, TimeUnit.MILLISECONDS);
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
                        return (ColumnData<K, V>) executor.submitWithPriority(
                                LoadDataJob.builder()
                                        .columnDataCache(cache)
                                        .datasource(datasource)
                                        .query(datasourceQuery)
                                        .build(),
                                Priority.HIGH,
                                datasourceQuery)
                                .get();
                    } catch (ExecutionException | InterruptedException e) {
                        log.error("Loading data from {} datasource has been failed", config.getRelationType(), e);
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public ColumnDataInfo getColumnDataInfo(Query query) {
        final DatasourceQuery datasourceQuery = datasource.toDatasourceQuery(query, config.getRelationType());
        return cache.getColumnCacheDetails(datasourceQuery.getDataColumn());
    }

    @Override
    public void preloadInBackground(Query query) {
        final DatasourceQuery datasourceQuery = datasource.toDatasourceQuery(query, config.getRelationType());
        if (cache.exist(datasourceQuery.getDataColumn())) {
            log.debug("Cache for table: " + query.getTablePair().getName() + " and column: "
                    + query.getColumnPair().getName() + " is already exist! Skipping.");
        } else if (executor.containsQuery(datasourceQuery)) {
            log.debug("Cache for table: " + query.getTablePair().getName() + " and column: "
                    + query.getColumnPair().getName() + " is in a caching queue! Skipping.");
        } else {
            executor.submitWithPriority(
                    CacheDataJob.builder()
                            .datasource(datasource)
                            .columnDataCache(cache)
                            .query(datasourceQuery)
                            .build(),
                    Priority.LOW,
                    datasourceQuery);
        }
    }

    @Override
    public void stopPreloadInBackground(Query query) {
        executor.remove(Priority.LOW, datasource.toDatasourceQuery(query, config.getRelationType()));
    }

    @Override
    public CachingStatus getCachingStatus(Query query) {
        if (cache.exist(query.getColumnPair().getColumnFor(config.getRelationType()))) {
            return CachingStatus.FINISHED;
        } else {
            final DatasourceQuery datasourceQuery = datasource.toDatasourceQuery(query, config.getRelationType());
            return (executor.containsQuery(datasourceQuery)) ? RUNNING : NON_DEFINED;
        }
    }

    @Override
    public void deleteCache(Query query) {
        cache.delete(query.getColumnPair().getColumnFor(config.getRelationType()));
    }
}
