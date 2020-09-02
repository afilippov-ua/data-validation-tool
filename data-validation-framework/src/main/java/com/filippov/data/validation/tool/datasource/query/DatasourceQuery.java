package com.filippov.data.validation.tool.datasource.query;

import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class DatasourceQuery {
    private DatasourceTable table;
    private DatasourceColumn keyColumn;
    private DatasourceColumn dataColumn;
}
