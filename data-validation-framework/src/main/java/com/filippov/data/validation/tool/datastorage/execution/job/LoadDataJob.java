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
