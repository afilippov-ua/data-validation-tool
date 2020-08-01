package com.filippov.data.validation.tool.validation.transformer.basic;

import com.filippov.data.validation.tool.model.DataType;
import com.filippov.data.validation.tool.validation.transformer.AbstractTransformer;

import java.util.Objects;

public class ObjectToStringTransformer extends AbstractTransformer {

    @Override
    public String transform(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return (String) value;
        } else {
            return Objects.toString(value);
        }
    }

    @Override
    public DataType getInputDataType() {
        return DataType.OBJECT;
    }

    @Override
    public DataType getOutputDataType() {
        return DataType.STRING;
    }
}
