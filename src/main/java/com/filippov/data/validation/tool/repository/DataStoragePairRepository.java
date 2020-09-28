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

package com.filippov.data.validation.tool.repository;

import com.filippov.data.validation.tool.factory.DataStorageFactory;
import com.filippov.data.validation.tool.factory.DatasourceFactory;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.DataStoragePair;
import com.filippov.data.validation.tool.repository.cache.WorkspaceRepositoryCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;

@Slf4j
@Component
public class DataStoragePairRepository {
    private final WorkspaceRepositoryCache cache;
    private final DataStorageFactory dataStorageFactory;
    private final DatasourceFactory datasourceFactory;

    public DataStoragePairRepository(DataStorageFactory dataStorageFactory, DatasourceFactory datasourceFactory, WorkspaceRepositoryCache cache) {
        this.dataStorageFactory = dataStorageFactory;
        this.datasourceFactory = datasourceFactory;
        this.cache = cache;
    }

    public DataStoragePair getOrLoad(Workspace workspace) {
        return cache.computeIfAbsent(workspace, (ws) ->
                DataStoragePair.builder()
                        .leftDataStorage(dataStorageFactory.create(
                                datasourceFactory.create(ws.getLeftDatasourceConfig()),
                                LEFT,
                                ws.getLeftDatasourceConfig().getMaxConnections()))
                        .rightDataStorage(dataStorageFactory.create(
                                datasourceFactory.create(ws.getRightDatasourceConfig()),
                                RIGHT,
                                ws.getRightDatasourceConfig().getMaxConnections()))
                        .build());
    }

    public void removeByWorkspace(Workspace workspace) {
        if (cache.containsKey(workspace)) {
            final DataStoragePair dsPair = cache.get(workspace);
            dsPair.getLeftDataStorage().deleteCache();
            dsPair.getRightDataStorage().deleteCache();
            cache.remove(workspace);
        }
    }
}
