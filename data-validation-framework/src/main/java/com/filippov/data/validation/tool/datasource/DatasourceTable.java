package com.filippov.data.validation.tool.datasource;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@JsonDeserialize(builder = DatasourceTable.DatasourceTableBuilder.class)
public class DatasourceTable {
    private String name;
    private DatasourceColumn primaryKey;
    private List<DatasourceColumn> columns;
}
