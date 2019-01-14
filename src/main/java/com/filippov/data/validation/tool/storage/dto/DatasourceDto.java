package com.filippov.data.validation.tool.storage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DatasourceDto {
    private String datasourceClass;
    private String connectionString;
}
