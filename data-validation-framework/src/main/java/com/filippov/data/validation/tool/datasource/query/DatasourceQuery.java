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
    private final DatasourceTable table;
    private final DatasourceColumn keyColumn;
    private final DatasourceColumn dataColumn;


    public String toString() {
        return "DatasourceQuery(keyColumn=" + this.getKeyColumn() + ", dataColumn=" + this.getDataColumn() + ")";
    }
}
