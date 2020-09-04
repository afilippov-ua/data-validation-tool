package com.filippov.data.validation.tool.dto.datasource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.filippov.data.validation.tool.model.DataType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class DatasourceColumnDto {
    private final String tableName;
    private final String name;
    private final DataType dataType;

    @JsonCreator
    public DatasourceColumnDto(String tableName, String name, DataType dataType) {
        this.tableName = tableName;
        this.name = name;
        this.dataType = dataType;
    }
}
