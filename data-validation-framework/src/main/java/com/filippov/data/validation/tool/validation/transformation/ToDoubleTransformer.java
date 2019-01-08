package com.filippov.data.validation.tool.validation.transformation;

public class ToDoubleTransformer implements Transformer<Object, Double> {

    @Override
    public Double transform(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof String) {
            return Double.parseDouble((String) value);
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + value.getClass().getSimpleName());
        }
    }
}
