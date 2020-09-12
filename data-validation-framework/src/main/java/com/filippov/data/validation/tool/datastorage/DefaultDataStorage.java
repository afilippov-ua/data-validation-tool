/*
 *   Copyright 2018-2020 the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.datastorage.execution.Priority;
import com.filippov.data.validation.tool.datastorage.execution.executor.DataStorageExecutor;
import com.filippov.data.validation.tool.datastorage.execution.job.CacheDataJob;
import com.filippov.data.validation.tool.datastorage.execution.job.LoadDataJob;
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
        if (config == null) {
            throw new IllegalArgumentException("Incorrect input: data storage config is null");
        }
        if (datasource == null) {
            throw new IllegalArgumentException("Incorrect input: datasource is null");
        }
        if (cache == null) {
            throw new IllegalArgumentException("Incorrect input: column data cache is null");
        }
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
        log.debug("Fetching column data has been started with query: {} from storage: {} and datasource: {}", query, config, datasource);
        final ColumnData<K, V> result = cache.getOrLoad(
                query.getColumnPair().getColumnFor(config.getRelationType()),
                () -> {
                    try {
                        final DatasourceQuery datasourceQuery = datasource.toDatasourceQuery(query, config.getRelationType());
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
                        throw new RuntimeException("Loading data from " + config.getRelationType() + " datasource: " + datasource +
                                " with query: " + query + " has been failed", e);
                    }
                });
        log.debug("Fetching column data has been finished with query: {} from storage: {} and datasource: {}", query, config, datasource);
        return result;
    }

    @Override
    public ColumnDataInfo getColumnDataInfo(Query query) {
        log.debug("Fetching column data info has been started for {} storage, datasource: {} and config: {} with query: {}",
                config.getRelationType(), datasource, config, query);
        final DatasourceQuery datasourceQuery = datasource.toDatasourceQuery(query, config.getRelationType());
        final ColumnDataInfo result = cache.getColumnCacheDetails(datasourceQuery.getDataColumn());
        log.debug("Fetching column data info has been finished for {} storage, datasource: {} and config: {} with query: {}",
                config.getRelationType(), datasource, config, query);
        return result;
    }

    @Override
    public void preloadInBackground(Query query) {
        log.debug("Starting preloading in background from {} storage, datasource: {} and config: {} with query: {}",
                config.getRelationType(), datasource, config, query);
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
        log.debug("Stopping preloading in background from {} storage, datasource: {} and config: {} with query: {}",
                config.getRelationType(), datasource, config, query);
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
        log.debug("Deleting cache from {} storage, datasource: {} and config: {} with query: {}", config.getRelationType(), datasource, config, query);
        cache.delete(query.getColumnPair().getColumnFor(config.getRelationType()));
    }
}
