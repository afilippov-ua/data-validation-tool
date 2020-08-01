package com.filippov.data.validation.tool.datasource.query;

import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class DatasourceQuery {
    private DatasourceTable table;
    private DatasourceColumn keyColumn;
    private DatasourceColumn dataColumn;
}
