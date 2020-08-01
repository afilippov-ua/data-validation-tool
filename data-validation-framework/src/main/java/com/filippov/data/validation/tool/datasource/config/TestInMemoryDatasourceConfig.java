package com.filippov.data.validation.tool.datasource.config;

import com.filippov.data.validation.tool.datasource.model.DatasourceType;

import static com.filippov.data.validation.tool.datasource.model.DatasourceType.TEST_IN_MEMORY_DATASOURCE;

public class TestInMemoryDatasourceConfig implements DatasourceConfig {

    @Override
    public DatasourceType getDatasourceType() {
        return TEST_IN_MEMORY_DATASOURCE;
    }

    @Override
    public int getMaxConnections() {
        return 1;
    }
}
