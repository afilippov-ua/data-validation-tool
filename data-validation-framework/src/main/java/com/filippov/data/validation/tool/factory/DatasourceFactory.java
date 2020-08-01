package com.filippov.data.validation.tool.factory;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.config.DatasourceConfig;

public interface DatasourceFactory {

    Datasource create(DatasourceConfig datasourceConfig);
}
