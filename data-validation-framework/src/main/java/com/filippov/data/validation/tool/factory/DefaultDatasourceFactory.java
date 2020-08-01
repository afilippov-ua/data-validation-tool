package com.filippov.data.validation.tool.factory;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.JsonDatasource;
import com.filippov.data.validation.tool.datasource.config.DatasourceConfig;
import com.filippov.data.validation.tool.datasource.config.JsonDatasourceConfig;
import com.filippov.data.validation.tool.datasource.query.TestInMemoryDatasource;

public class DefaultDatasourceFactory implements DatasourceFactory {

    @Override
    public Datasource create(DatasourceConfig datasourceConfig) {
        switch (datasourceConfig.getDatasourceType()) {
            case JSON_DATASOURCE:
                return new JsonDatasource((JsonDatasourceConfig) datasourceConfig);
            case TEST_IN_MEMORY_DATASOURCE:
                return new TestInMemoryDatasource();
            default:
                throw new IllegalArgumentException("Datasource type: " + datasourceConfig.getDatasourceType() + " doesn't supported.");
        }
    }
}
