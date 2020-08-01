package com.filippov.data.validation.tool.dto.validation;

import com.filippov.data.validation.tool.model.DataType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransformerDto {
    private String name;
    private DataType inputDataType;
    private DataType outputDataType;
}
