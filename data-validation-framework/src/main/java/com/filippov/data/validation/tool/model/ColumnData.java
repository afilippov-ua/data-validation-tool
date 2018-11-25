package com.filippov.data.validation.tool.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonDeserialize(builder = ColumnData.ColumnDataBuilder.class)
public class ColumnData {
    private DatasourceColumn primaryKey;
    private DatasourceColumn column;
    private List<?> keys;
    private List<?> values;

    public ColumnData(DatasourceColumn primaryKey, DatasourceColumn column, List<?> keys, List<?> values) {
        this.primaryKey = primaryKey;
        this.column = column;
        this.keys = keys;
        this.values = values;
    }
}