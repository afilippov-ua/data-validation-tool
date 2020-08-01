package com.filippov.data.validation.tool.dto.datasource;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class DatasourcePairDto {
    private DatasourceDefinitionDto leftDatasource;
    private DatasourceDefinitionDto rightDatasource;
}
