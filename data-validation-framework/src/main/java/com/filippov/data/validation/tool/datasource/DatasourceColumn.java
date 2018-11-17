package com.filippov.data.validation.tool.datasource;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.filippov.data.validation.tool.model.DataType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonDeserialize(builder = DatasourceColumn.DatasourceColumnBuilder.class)
public class DatasourceColumn {
    private String name;
    private DataType dataType;
}
