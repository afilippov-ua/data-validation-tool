package com.filippov.data.validation.tool.validation.transformer.specific.string;

import com.filippov.data.validation.tool.validation.transformer.Transformer;

public class TrimStringTransformer implements Transformer<String, String> {

    @Override
    public String transform(String value) {
        if (value == null) {
            return null;
        } else {
            return value.trim();
        }
    }
}
