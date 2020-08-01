package com.filippov.data.validation.tool.validation.transformer.basic;

import com.filippov.data.validation.tool.model.DataType;
import com.filippov.data.validation.tool.validation.transformer.AbstractTransformer;

public class ObjectToIntegerTransformer extends AbstractTransformer {

    @Override
    public Integer transform(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        } else if (value instanceof Double) {
            return ((Double) value).intValue(); // TODO?
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + value.getClass().getSimpleName());
        }
    }

    @Override
    public DataType getInputDataType() {
        return DataType.OBJECT;
    }

    @Override
    public DataType getOutputDataType() {
        return DataType.INTEGER;
    }
}
