package com.filippov.data.validation.tool.storage.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class DatasourceDto {
    private String datasourceClass;
    private String connectionString;
}
