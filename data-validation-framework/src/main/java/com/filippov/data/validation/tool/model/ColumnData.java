package com.filippov.data.validation.tool.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@EqualsAndHashCode(of = {"primaryKey", "column"})
@JsonDeserialize(builder = ColumnData.ColumnDataBuilder.class)
public class ColumnData<K, V> implements Serializable {
    private DatasourceColumn primaryKey;
    private DatasourceColumn column;
    private List<K> keys;
    private List<V> values;

    public ColumnData(DatasourceColumn primaryKey, DatasourceColumn column, List<K> keys, List<V> values) {
        this.primaryKey = primaryKey;
        this.column = column;
        this.keys = keys;
        this.values = values;
    }
}