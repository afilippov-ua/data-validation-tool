package com.filippov.data.validation.tool.repository;

import com.filippov.data.validation.tool.factory.DataStorageFactory;
import com.filippov.data.validation.tool.factory.DatasourceFactory;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.DataStoragePair;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;

@Component
public class DataStoragePairRepository {
    private final Map<Workspace, DataStoragePair> cache;
    private final DataStorageFactory dataStorageFactory;
    private final DatasourceFactory datasourceFactory;

    public DataStoragePairRepository(DataStorageFactory dataStorageFactory, DatasourceFactory datasourceFactory) {
        this.dataStorageFactory = dataStorageFactory;
        this.datasourceFactory = datasourceFactory;
        this.cache = new ConcurrentHashMap<>();
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
}
