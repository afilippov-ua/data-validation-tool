package com.filippov.data.validation.tool.dto.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.filippov.data.validation.tool.model.DataType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TransformerDto {
    private final String name;
    private final DataType inputDataType;
    private final DataType outputDataType;

    @JsonCreator
    public TransformerDto(String name, DataType inputDataType, DataType outputDataType) {
        this.name = name;
        this.inputDataType = inputDataType;
        this.outputDataType = outputDataType;
    }
}
