package com.filippov.data.validation.tool.dto.datasource;

import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class DatasourceDefinitionDto {
    private DatasourceType datasourceType;
    private String config;
}
