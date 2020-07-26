package com.filippov.data.validation.tool.factory;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datastorage.DataStorage;
import com.filippov.data.validation.tool.datastorage.RelationType;

public interface DataStorageFactory {

    DataStorage create(Datasource datasource, RelationType relationType, Integer maxConnections);
}
