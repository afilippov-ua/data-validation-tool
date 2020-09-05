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

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datastorage.DataStorage;
import com.filippov.data.validation.tool.datastorage.DataStorageConfig;
import com.filippov.data.validation.tool.datastorage.DefaultDataStorage;
import com.filippov.data.validation.tool.datastorage.RelationType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultDataStorageFactory implements DataStorageFactory {
    private final ColumnDataCacheFactory columnDataCacheFactory;

    public DefaultDataStorageFactory(ColumnDataCacheFactory columnDataCacheFactory) {
        this.columnDataCacheFactory = columnDataCacheFactory;
    }

    @Override
    public DataStorage create(Datasource datasource, RelationType relationType, Integer maxConnections) {
        log.debug("Creating data storage for datasource: {}, relation type: {} and maxConnection: {}", datasource, relationType, maxConnections);
        return DefaultDataStorage.builder()
                .config(DataStorageConfig.builder()
                        .relationType(relationType)
                        .maxConnections(maxConnections)
                        .build())
                .datasource(datasource)
                .cache(columnDataCacheFactory.getOrCreateForDatasource(datasource))
                .build();
    }
}
