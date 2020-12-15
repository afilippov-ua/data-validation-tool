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

package com.filippov.data.validation.tool.factory;

import com.filippov.data.validation.tool.datastorage.DefaultDataStorage;
import com.filippov.data.validation.tool.model.ColumnDataCacheFactory;
import com.filippov.data.validation.tool.model.DataStorage;
import com.filippov.data.validation.tool.model.DataStorageFactory;
import com.filippov.data.validation.tool.model.Datasource;
import com.filippov.data.validation.tool.model.datastorage.DataStorageConfig;
import com.filippov.data.validation.tool.model.datastorage.RelationType;
import com.filippov.data.validation.tool.model.workspace.Workspace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultDataStorageFactory implements DataStorageFactory {
    private final ColumnDataCacheFactory columnDataCacheFactory;

    public DefaultDataStorageFactory(ColumnDataCacheFactory columnDataCacheFactory) {
        if (columnDataCacheFactory == null) {
            throw new IllegalArgumentException("Incorrect input: columnDataCacheFactory is null");
        }
        this.columnDataCacheFactory = columnDataCacheFactory;
    }

    @Override
    public DataStorage create(Workspace workspace, Datasource datasource, RelationType relationType, Integer maxConnections) {
        if (datasource == null) {
            throw new IllegalArgumentException("Incorrect input: datasource is null");
        }
        if (relationType == null) {
            throw new IllegalArgumentException("Incorrect input: relationType is null");
        }
        if (maxConnections == null) {
            throw new IllegalArgumentException("Incorrect input: maxConnections is null");
        }
        if (maxConnections <= 0) {
            throw new IllegalArgumentException("Incorrect input: maxConnections is less or equal zero");
        }
        log.debug("Creating data storage for datasource: {}, relation type: {} and maxConnection: {}", datasource, relationType, maxConnections);
        return DefaultDataStorage.builder()
                .config(DataStorageConfig.builder()
                        .relationType(relationType)
                        .maxConnections(maxConnections)
                        .build())
                .datasource(datasource)
                .cache(columnDataCacheFactory.getOrCreateForDatasource(workspace, datasource, relationType))
                .build();
    }
}
