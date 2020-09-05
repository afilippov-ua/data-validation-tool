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
