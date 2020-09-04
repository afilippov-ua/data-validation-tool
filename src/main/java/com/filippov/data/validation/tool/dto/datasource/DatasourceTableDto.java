package com.filippov.data.validation.tool.dto.datasource;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class DatasourceTableDto {
    private final String name;
    private final String primaryKey;
    private final List<String> columns;

    @JsonCreator
    public DatasourceTableDto(String name, String primaryKey, List<String> columns) {
        this.name = name;
        this.primaryKey = primaryKey;
        this.columns = columns;
    }
}
