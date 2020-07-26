package com.filippov.data.validation.tool.datasource;

import com.filippov.data.validation.tool.datasource.model.DatasourceConfig;
import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.model.ColumnData;

public interface Datasource {

    DatasourceConfig getConfig();

    DatasourceMetadata getMetadata();

    <K, V> ColumnData<K, V> getColumnData(DatasourceQuery query);
}
