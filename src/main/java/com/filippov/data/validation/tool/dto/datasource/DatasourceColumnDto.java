package com.filippov.data.validation.tool.dto.datasource;

import com.filippov.data.validation.tool.model.DataType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class DatasourceColumnDto {
    private String tableName;
    private String name;
    private DataType dataType;
}
