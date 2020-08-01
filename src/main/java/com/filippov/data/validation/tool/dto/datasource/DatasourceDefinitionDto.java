package com.filippov.data.validation.tool.dto.datasource;

import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@EqualsAndHashCode
public class DatasourceDefinitionDto {
    private DatasourceType datasourceType;
    private Integer maxConnections;
    private Map<String, Object> configParams;
}
