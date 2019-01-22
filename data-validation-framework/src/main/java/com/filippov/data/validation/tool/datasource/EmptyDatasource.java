package com.filippov.data.validation.tool.datasource;

import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.model.ColumnData;

import static java.util.Collections.emptyList;

public class EmptyDatasource implements Datasource {
    private final String connectionString;

    @Override
    public DatasourceType getDatasourceType() {
        return DatasourceType.EMPTY_DATASOURCE;
    }

    public EmptyDatasource(String connectionString) {
        this.connectionString = connectionString;
    }

    @Override
    public String getConnectionString() {
        return connectionString;
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
}
