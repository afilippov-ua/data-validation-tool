package com.filippov.data.validation.tool.dto.datasource;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class DatasourcePairDto {
    private final DatasourceDefinitionDto leftDatasource;
    private final DatasourceDefinitionDto rightDatasource;

    @JsonCreator
    public DatasourcePairDto(DatasourceDefinitionDto leftDatasource, DatasourceDefinitionDto rightDatasource) {
        this.leftDatasource = leftDatasource;
        this.rightDatasource = rightDatasource;
    }
}
