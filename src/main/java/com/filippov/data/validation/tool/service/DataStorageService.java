package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datastorage.DataStorage;
import com.filippov.data.validation.tool.datastorage.RelationType;
import com.filippov.data.validation.tool.factory.DataStorageFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataStorageService {
    private final Map<Datasource, DataStorage> cache;
    private final DataStorageFactory dataStorageFactory;

    public DataStorageService(DataStorageFactory dataStorageFactory) {
        this.dataStorageFactory = dataStorageFactory;
        this.cache = new ConcurrentHashMap<>();
    }

    public DataStorage getOrCreate(Datasource datasource, RelationType relationType) {
        return cache.computeIfAbsent(datasource,
                (ds) -> dataStorageFactory.create(ds, relationType, datasource.getConfig().getDefaultMaxConnections()));
    }


}
