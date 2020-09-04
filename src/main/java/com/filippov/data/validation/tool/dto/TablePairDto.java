package com.filippov.data.validation.tool.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.filippov.data.validation.tool.dto.datasource.DatasourceTableDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class TablePairDto {
    private final String id;
    private final String name;
    private final DatasourceTableDto leftDatasourceTable;
    private final DatasourceTableDto rightDatasourceTable;

    @JsonCreator
    public TablePairDto(String id, String name, DatasourceTableDto leftDatasourceTable, DatasourceTableDto rightDatasourceTable) {
        this.id = id;
        this.name = name;
        this.leftDatasourceTable = leftDatasourceTable;
        this.rightDatasourceTable = rightDatasourceTable;
    }
}
