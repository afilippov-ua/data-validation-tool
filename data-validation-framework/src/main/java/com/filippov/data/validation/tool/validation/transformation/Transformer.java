package com.filippov.data.validation.tool.validation.transformation;

public interface Transformer<Input, Output> {
    Output transform(Input value);
}
