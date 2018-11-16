package com.filippov.data.validation.tool.validation.transformation;

public class TrimStringTransformer implements Transformer<String, String> {

    @Override
    public String transform(String value) {
        return value.trim();
    }
}
