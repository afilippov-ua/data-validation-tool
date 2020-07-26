package com.filippov.data.validation.tool.factory;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.model.DatasourceConfig;
import com.filippov.data.validation.tool.datasource.EmptyDatasource;
import com.filippov.data.validation.tool.datasource.TestDatasource;

public class DefaultDatasourceFactory implements DatasourceFactory {

    @Override
    public Datasource create(DatasourceConfig datasourceConfig) {
        switch (datasourceConfig.getDatasourceType()) {
            case EMPTY_DATASOURCE:
                return new EmptyDatasource(datasourceConfig);
            case TEST_DATASOURCE:
                return new TestDatasource(); // TODO
            case JSON_DATASOURCE:
                throw new IllegalArgumentException("Json datasource is not implemented yet.");
            default:
                throw new IllegalArgumentException("Datasource type: " + datasourceConfig.getDatasourceType() + " doesn't supported.");
        }
    }
}
