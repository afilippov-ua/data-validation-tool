package com.filippov.data.validation.tool.datasource;

import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.model.ColumnData;

public interface Datasource {

    String getId();

    DatasourceMetadata getMetadata();

    ColumnData getColumnData(DatasourceQuery query);
}
