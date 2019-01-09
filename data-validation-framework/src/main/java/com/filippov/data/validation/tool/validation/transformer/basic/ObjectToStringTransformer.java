package com.filippov.data.validation.tool.validation.transformer.basic;

import com.filippov.data.validation.tool.validation.transformer.Transformer;

import java.util.Objects;

public class ObjectToStringTransformer implements Transformer<Object, String> {

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
}
