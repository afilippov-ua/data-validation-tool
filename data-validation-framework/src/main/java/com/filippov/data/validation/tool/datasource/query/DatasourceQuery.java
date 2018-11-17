package com.filippov.data.validation.tool.datasource.query;

import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.DatasourceTable;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class DatasourceQuery {
    private DatasourceTable table;
    private DatasourceColumn primaryKey;
    private DatasourceColumn column;
    private Map<String, Object> queryParams;
}
