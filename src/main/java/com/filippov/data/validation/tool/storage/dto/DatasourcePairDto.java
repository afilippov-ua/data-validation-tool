package com.filippov.data.validation.tool.storage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DatasourcePairDto {
    private DatasourceDto left;
    private DatasourceDto right;
}
