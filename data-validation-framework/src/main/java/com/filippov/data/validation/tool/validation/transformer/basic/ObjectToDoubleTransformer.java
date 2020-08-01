package com.filippov.data.validation.tool.validation.transformer.basic;

import com.filippov.data.validation.tool.model.DataType;
import com.filippov.data.validation.tool.validation.transformer.AbstractTransformer;

public class ObjectToDoubleTransformer extends AbstractTransformer {

    @Override
    public Double transform(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof String) {
            return Double.parseDouble((String) value);
        } else if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
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
        return DataType.DOUBLE;
    }
}
