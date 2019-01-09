package com.filippov.data.validation.tool.validation.transformer;

public interface Transformer<Input, Output> {
    Output transform(Input value);
}
