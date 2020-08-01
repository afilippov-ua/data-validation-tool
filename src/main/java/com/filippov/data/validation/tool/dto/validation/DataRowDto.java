package com.filippov.data.validation.tool.dto.validation;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DataRowDto {
    private Object key;
    private Object leftOriginalValue;
    private Object rightOriginalValue;
    private Object leftTransformedValue;
    private Object rightTransformedValue;
}
