package com.filippov.data.validation.tool.dto.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class DataRowDto {
    private final Object key;
    private final Object leftOriginalValue;
    private final Object rightOriginalValue;
    private final Object leftTransformedValue;
    private final Object rightTransformedValue;

    @JsonCreator
    public DataRowDto(Object key, Object leftOriginalValue, Object rightOriginalValue, Object leftTransformedValue, Object rightTransformedValue) {
        this.key = key;
        this.leftOriginalValue = leftOriginalValue;
        this.rightOriginalValue = rightOriginalValue;
        this.leftTransformedValue = leftTransformedValue;
        this.rightTransformedValue = rightTransformedValue;
    }
}
