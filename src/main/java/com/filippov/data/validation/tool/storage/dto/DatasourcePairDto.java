package com.filippov.data.validation.tool.storage.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class DatasourcePairDto {
    private DatasourceDto left;
    private DatasourceDto right;
}
