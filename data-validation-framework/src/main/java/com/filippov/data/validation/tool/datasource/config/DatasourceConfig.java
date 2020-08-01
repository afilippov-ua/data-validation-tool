package com.filippov.data.validation.tool.datasource.config;

import com.filippov.data.validation.tool.datasource.model.DatasourceType;

public interface DatasourceConfig {
    DatasourceType getDatasourceType();

    int getMaxConnections();
}
