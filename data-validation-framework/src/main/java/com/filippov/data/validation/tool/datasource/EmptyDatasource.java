package com.filippov.data.validation.tool.datasource;

import com.filippov.data.validation.tool.datasource.model.DatasourceConfig;
import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.model.ColumnData;

import static java.util.Collections.emptyList;

public class EmptyDatasource implements Datasource {
    private final DatasourceConfig datasourceConfig;

    public EmptyDatasource(DatasourceConfig datasourceConfig) {
        this.datasourceConfig = datasourceConfig;
    }

    @Override
    public DatasourceMetadata getMetadata() {
        return DatasourceMetadata.builder()
                .tables(emptyList())
                .columns(emptyList())
                .build();
    }

    @Override
    public <K, V> ColumnData<K, V> getColumnData(DatasourceQuery query) {
        return null;
    }

    @Override
    public DatasourceConfig getConfig() {
        return datasourceConfig;
    }
}