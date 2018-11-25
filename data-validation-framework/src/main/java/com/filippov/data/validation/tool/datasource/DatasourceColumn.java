package com.filippov.data.validation.tool.datasource;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.filippov.data.validation.tool.model.DataType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(of = {"table", "name"})
@JsonDeserialize(builder = DatasourceColumn.DatasourceColumnBuilder.class)
public class DatasourceColumn {
    private DatasourceTable table;
    private String name;
    private DataType dataType;
}
