package com.filippov.data.validation.tool.dto.datasource;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
public class DatasourceTableDto {
    private String name;
    private String primaryKey;
    private List<String> columns;
}
