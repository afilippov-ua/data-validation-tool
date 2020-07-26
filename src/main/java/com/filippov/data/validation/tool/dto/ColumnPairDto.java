package com.filippov.data.validation.tool.dto;

import com.filippov.data.validation.tool.dto.datasource.DatasourceColumnDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class ColumnPairDto {
    private String tablePairName;
    private String name;
    private DatasourceColumnDto left;
    private DatasourceColumnDto right;
}
