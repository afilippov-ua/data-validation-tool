package com.filippov.data.validation.tool.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = ColumnData.ColumnDataBuilder.class)
public class ColumnData {
    private DatasourceColumn primaryKey;
    private DatasourceColumn column;
    private Object[] keys;
    private Object[] values;
}