package com.filippov.data.validation.tool.dto;

import com.filippov.data.validation.tool.dto.datasource.DatasourceTableDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class TablePairDto {
    private String id;
    private String name;
    private DatasourceTableDto leftDatasourceTable;
    private DatasourceTableDto rightDatasourceTable;
}
