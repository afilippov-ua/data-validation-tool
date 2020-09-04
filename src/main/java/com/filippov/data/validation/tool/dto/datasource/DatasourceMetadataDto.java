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
public class DatasourceMetadataDto {
    private final List<DatasourceTableDto> tables;
    private final List<DatasourceColumnDto> columns;

    @JsonCreator
    public DatasourceMetadataDto(List<DatasourceTableDto> tables, List<DatasourceColumnDto> columns) {
        this.tables = tables;
        this.columns = columns;
    }
}
