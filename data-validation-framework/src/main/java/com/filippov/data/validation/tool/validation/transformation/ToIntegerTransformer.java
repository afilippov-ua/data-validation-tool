package com.filippov.data.validation.tool.validation.transformation;

public class ToIntegerTransformer implements Transformer<Object, Integer> {

    @Override
    public Integer transform(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + value.getClass().getSimpleName());
        }
    }
}
