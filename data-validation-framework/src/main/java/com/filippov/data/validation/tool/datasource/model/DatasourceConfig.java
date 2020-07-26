package com.filippov.data.validation.tool.datasource.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class DatasourceConfig {
    private DatasourceType datasourceType;
    private Integer defaultMaxConnections;
    private String config; // TODO
}
