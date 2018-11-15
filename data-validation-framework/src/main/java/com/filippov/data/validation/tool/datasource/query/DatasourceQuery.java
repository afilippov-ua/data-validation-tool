package com.filippov.data.validation.tool.datasource.query;

import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.DatasourceTable;

import java.util.Map;

public class DatasourceQuery {
    private DatasourceTable table;
    private DatasourceColumn primaryKey;
    private DatasourceColumn column;
    private Map<String, Object> queryParams;
}
