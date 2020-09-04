package com.filippov.data.validation.tool.dto.datasource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class DatasourceDefinitionDto {
    private final DatasourceType datasourceType;
    private final Integer maxConnections;
    private final Map<String, Object> configParams;

    @JsonCreator
    public DatasourceDefinitionDto(DatasourceType datasourceType, Integer maxConnections, Map<String, Object> configParams) {
        this.datasourceType = datasourceType;
        this.maxConnections = maxConnections;
        this.configParams = configParams;
    }
}
