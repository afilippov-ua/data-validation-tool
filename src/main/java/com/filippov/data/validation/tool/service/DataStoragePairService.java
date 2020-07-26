package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.DataStoragePair;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;

@Component
public class DataStoragePairService {
    private final DataStorageService dataStorageService;
    private final DatasourceService datasourceService;
    private final Map<Workspace, DataStoragePair> cache;

    public DataStoragePairService(DataStorageService dataStorageService, DatasourceService datasourceService) {
        this.dataStorageService = dataStorageService;
        this.datasourceService = datasourceService;
        this.cache = new ConcurrentHashMap<>();
    }

    public DataStoragePair getOrLoad(Workspace workspace) {
        return cache.computeIfAbsent(workspace, (ws) -> DataStoragePair.builder()
                .left(dataStorageService.getOrCreate(datasourceService.getLeftOrCreate(ws), LEFT))
                .right(dataStorageService.getOrCreate(datasourceService.getRightOrCreate(ws), RIGHT))
                .build());
    }
}
