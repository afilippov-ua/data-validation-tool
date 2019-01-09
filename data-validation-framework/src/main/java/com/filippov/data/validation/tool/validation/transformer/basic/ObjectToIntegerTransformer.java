package com.filippov.data.validation.tool.validation.transformer.basic;

import com.filippov.data.validation.tool.validation.transformer.Transformer;

public class ObjectToIntegerTransformer implements Transformer<Object, Integer> {

    @Override
    public Integer transform(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        } else if (value instanceof Double) {
            return ((Double) value).intValue();
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + value.getClass().getSimpleName());
        }
    }
}
