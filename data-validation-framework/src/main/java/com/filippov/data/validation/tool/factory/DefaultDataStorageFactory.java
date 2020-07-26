package com.filippov.data.validation.tool.factory;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datastorage.DataStorage;
import com.filippov.data.validation.tool.datastorage.DataStorageConfig;
import com.filippov.data.validation.tool.datastorage.DefaultDataStorage;
import com.filippov.data.validation.tool.datastorage.RelationType;

public class DefaultDataStorageFactory implements DataStorageFactory {
    private ColumnDataCacheFactory columnDataCacheFactory;

    public DefaultDataStorageFactory(ColumnDataCacheFactory columnDataCacheFactory) {
        this.columnDataCacheFactory = columnDataCacheFactory;
    }

    @Override
    public DataStorage create(Datasource datasource, RelationType relationType, Integer maxConnections) {
        return DefaultDataStorage.builder()
                .config(DataStorageConfig.builder()
                        .relationType(relationType)
                        .datasource(datasource)
                        .maxConnections(maxConnections)
                        .build())
                .cache(columnDataCacheFactory.getOrCreateForDatasource(datasource))
                .build();
    }
}
