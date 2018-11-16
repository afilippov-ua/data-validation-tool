package com.filippov.data.validation.tool.validation.transformation;

import java.util.Objects;

public class ToStringTransformer implements Transformer<Object, String> {

    @Override
    public String transform(Object value) {
        return Objects.toString(value);
    }
}
