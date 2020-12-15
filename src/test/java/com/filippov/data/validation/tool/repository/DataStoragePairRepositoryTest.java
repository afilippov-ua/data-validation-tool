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

import com.filippov.data.validation.tool.columndatacache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.datasource.TestInMemoryDatasource;
import com.filippov.data.validation.tool.datasource.TestInMemoryDatasourceConfig;
import com.filippov.data.validation.tool.datastorage.DefaultDataStorage;
import com.filippov.data.validation.tool.model.ColumnDataCache;
import com.filippov.data.validation.tool.model.DataStorage;
import com.filippov.data.validation.tool.model.DataStorageFactory;
import com.filippov.data.validation.tool.model.Datasource;
import com.filippov.data.validation.tool.model.DatasourceFactory;
import com.filippov.data.validation.tool.model.cache.CacheConfig;
import com.filippov.data.validation.tool.model.cache.EvictionStrategy;
import com.filippov.data.validation.tool.model.datastorage.DataStorageConfig;
import com.filippov.data.validation.tool.model.datastorage.RelationType;
import com.filippov.data.validation.tool.model.pair.DataStoragePair;
import com.filippov.data.validation.tool.model.workspace.Workspace;
import com.filippov.data.validation.tool.repository.cache.InMemoryWorkspaceRepositoryCache;
import com.filippov.data.validation.tool.repository.cache.WorkspaceRepositoryCache;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class DataStoragePairRepositoryTest {
    private static final ColumnDataCache LEFT_COLUMN_DATA_CACHE = new InMemoryColumnDataCache(CacheConfig.builder()
            .evictionStrategy(EvictionStrategy.FIFO)
            .maxNumberOfElementsInCache(1000)
            .build());
    private static final ColumnDataCache RIGHT_COLUMN_DATA_CACHE = new InMemoryColumnDataCache(CacheConfig.builder()
            .evictionStrategy(EvictionStrategy.FIFO)
            .maxNumberOfElementsInCache(1000)
            .build());

    private static final TestInMemoryDatasourceConfig LEFT_DS_CONFIG = TestInMemoryDatasourceConfig.builder()
            .relationType(RelationType.LEFT)
            .maxConnections(1)
            .build();
    private static final TestInMemoryDatasourceConfig RIGHT_DS_CONFIG = TestInMemoryDatasourceConfig.builder()
            .relationType(RelationType.RIGHT)
            .maxConnections(1)
            .build();

    private static final Workspace WORKSPACE = Workspace.builder()
            .id("test-id")
            .name("test-name")
            .leftDatasourceConfig(LEFT_DS_CONFIG)
            .rightDatasourceConfig(RIGHT_DS_CONFIG)
            .build();

    private static final Datasource LEFT_DS = new TestInMemoryDatasource(LEFT_DS_CONFIG);
    private static final Datasource RIGHT_DS = new TestInMemoryDatasource(RIGHT_DS_CONFIG);

    private static final DefaultDataStorage LEFT_STORAGE = DefaultDataStorage.builder()
            .datasource(LEFT_DS)
            .config(DataStorageConfig.builder()
                    .relationType(LEFT_DS_CONFIG.getRelation())
                    .maxConnections(LEFT_DS_CONFIG.getMaxConnections())
                    .build())
            .cache(LEFT_COLUMN_DATA_CACHE)
            .build();

    private static final DefaultDataStorage RIGHT_STORAGE = DefaultDataStorage.builder()
            .datasource(RIGHT_DS)
            .config(DataStorageConfig.builder()
                    .relationType(RIGHT_DS_CONFIG.getRelation())
                    .maxConnections(RIGHT_DS_CONFIG.getMaxConnections())
                    .build())
            .cache(RIGHT_COLUMN_DATA_CACHE)
            .build();

    @AfterEach
    void cleanUp() {
        LEFT_COLUMN_DATA_CACHE.deleteAll();
        RIGHT_COLUMN_DATA_CACHE.deleteAll();
    }

    @Test
    void getOrLoadDataStoragePairTest() {
        final DataStorageFactory dataStorageFactoryMock = Mockito.mock(DataStorageFactory.class);
        when(dataStorageFactoryMock.create(WORKSPACE, LEFT_DS, LEFT_DS_CONFIG.getRelation(), LEFT_DS_CONFIG.getMaxConnections()))
                .thenReturn(LEFT_STORAGE);

        when(dataStorageFactoryMock.create(WORKSPACE, RIGHT_DS, RIGHT_DS_CONFIG.getRelation(), RIGHT_DS_CONFIG.getMaxConnections()))
                .thenReturn(RIGHT_STORAGE);

        final DatasourceFactory datasourceFactoryMock = Mockito.mock(DatasourceFactory.class);
        when(datasourceFactoryMock.create(LEFT_DS_CONFIG)).thenReturn(LEFT_DS);
        when(datasourceFactoryMock.create(RIGHT_DS_CONFIG)).thenReturn(RIGHT_DS);

        final DataStoragePair dsPair = new DataStoragePairRepository(dataStorageFactoryMock, datasourceFactoryMock, new InMemoryWorkspaceRepositoryCache())
                .getOrLoad(WORKSPACE);

        assertThat(dsPair).isNotNull();
        assertThat(dsPair.getLeftDataStorage()).isNotNull().isEqualTo(LEFT_STORAGE);
        assertThat(dsPair.getRightDataStorage()).isNotNull().isEqualTo(RIGHT_STORAGE);

        verify(datasourceFactoryMock, times(1)).create(LEFT_DS_CONFIG);
        verify(datasourceFactoryMock, times(1)).create(RIGHT_DS_CONFIG);

        verify(dataStorageFactoryMock, times(1))
                .create(WORKSPACE, LEFT_DS, LEFT_DS_CONFIG.getRelation(), LEFT_DS_CONFIG.getMaxConnections());
        verify(dataStorageFactoryMock, times(1))
                .create(WORKSPACE, RIGHT_DS, RIGHT_DS_CONFIG.getRelation(), RIGHT_DS_CONFIG.getMaxConnections());

        verifyNoMoreInteractions(datasourceFactoryMock);
        verifyNoMoreInteractions(dataStorageFactoryMock);
    }

    @Test
    void checkTheSecondCallOfGetOrLoadDoesNotCreatePairOneMoreTimeTest() {
        final DataStorageFactory dataStorageFactoryMock = Mockito.mock(DataStorageFactory.class);
        when(dataStorageFactoryMock.create(WORKSPACE, LEFT_DS, LEFT_DS_CONFIG.getRelation(), LEFT_DS_CONFIG.getMaxConnections()))
                .thenReturn(LEFT_STORAGE);

        when(dataStorageFactoryMock.create(WORKSPACE, RIGHT_DS, RIGHT_DS_CONFIG.getRelation(), RIGHT_DS_CONFIG.getMaxConnections()))
                .thenReturn(RIGHT_STORAGE);

        final DatasourceFactory datasourceFactoryMock = Mockito.mock(DatasourceFactory.class);
        when(datasourceFactoryMock.create(LEFT_DS_CONFIG)).thenReturn(LEFT_DS);
        when(datasourceFactoryMock.create(RIGHT_DS_CONFIG)).thenReturn(RIGHT_DS);

        final DataStoragePairRepository repository = new DataStoragePairRepository(dataStorageFactoryMock, datasourceFactoryMock, new InMemoryWorkspaceRepositoryCache());
        final Workspace workspace = Workspace.builder()
                .id("test-id")
                .name("test-name")
                .leftDatasourceConfig(LEFT_DS_CONFIG)
                .rightDatasourceConfig(RIGHT_DS_CONFIG)
                .build();

        final DataStoragePair firstDsPair = repository.getOrLoad(workspace);

        assertThat(firstDsPair).isNotNull();
        assertThat(firstDsPair.getLeftDataStorage()).isNotNull().isEqualTo(LEFT_STORAGE);
        assertThat(firstDsPair.getRightDataStorage()).isNotNull().isEqualTo(RIGHT_STORAGE);

        verify(datasourceFactoryMock, times(1)).create(LEFT_DS_CONFIG);
        verify(datasourceFactoryMock, times(1)).create(RIGHT_DS_CONFIG);

        verify(dataStorageFactoryMock, times(1))
                .create(WORKSPACE, LEFT_DS, LEFT_DS_CONFIG.getRelation(), LEFT_DS_CONFIG.getMaxConnections());
        verify(dataStorageFactoryMock, times(1))
                .create(WORKSPACE, RIGHT_DS, RIGHT_DS_CONFIG.getRelation(), RIGHT_DS_CONFIG.getMaxConnections());

        verifyNoMoreInteractions(datasourceFactoryMock);
        verifyNoMoreInteractions(dataStorageFactoryMock);

        Mockito.reset(datasourceFactoryMock, dataStorageFactoryMock);

        // try to getOrLoad the dsPair for the second time
        final DataStoragePair secondDsPair = repository.getOrLoad(workspace);

        assertThat(firstDsPair)
                .isNotNull()
                .isEqualTo(firstDsPair);

        verifyNoInteractions(datasourceFactoryMock);
        verifyNoInteractions(dataStorageFactoryMock);
    }

    @Test
    void removeByWorkspaceTest() {
        final Workspace workspace = Workspace.builder()
                .id("test-id")
                .name("test-name")
                .leftDatasourceConfig(LEFT_DS_CONFIG)
                .rightDatasourceConfig(RIGHT_DS_CONFIG)
                .build();

        final DataStorage leftDsMock = Mockito.mock(DataStorage.class);
        doNothing().when(leftDsMock).deleteCache();
        final DataStorage rightDsMock = Mockito.mock(DataStorage.class);
        doNothing().when(rightDsMock).deleteCache();

        final DataStoragePair dsPair = DataStoragePair.builder()
                .leftDataStorage(leftDsMock)
                .rightDataStorage(rightDsMock)
                .build();

        final DataStorageFactory dataStorageFactoryMock = Mockito.mock(DataStorageFactory.class);
        final DatasourceFactory datasourceFactoryMock = Mockito.mock(DatasourceFactory.class);

        final WorkspaceRepositoryCache cacheMock = Mockito.mock(WorkspaceRepositoryCache.class);
        when(cacheMock.containsKey(workspace)).thenReturn(true);
        when(cacheMock.get(workspace)).thenReturn(dsPair);
        when(cacheMock.remove(workspace)).thenReturn(dsPair);

        final DataStoragePairRepository repository = new DataStoragePairRepository(dataStorageFactoryMock, datasourceFactoryMock, cacheMock);
        repository.removeByWorkspace(workspace);

        verify(cacheMock, times(1)).containsKey(workspace);
        verify(cacheMock, times(1)).get(workspace);
        verify(cacheMock, times(1)).remove(workspace);
        verifyNoMoreInteractions(cacheMock);

        verify(leftDsMock, times(1)).deleteCache();
        verifyNoMoreInteractions(leftDsMock);

        verify(rightDsMock, times(1)).deleteCache();
        verifyNoMoreInteractions(rightDsMock);

        verifyNoInteractions(dataStorageFactoryMock);
        verifyNoInteractions(datasourceFactoryMock);
    }
}
