package com.filippov.data.validation.tool.validation.transformer.specific.string;

import com.filippov.data.validation.tool.model.DataType;
import com.filippov.data.validation.tool.validation.transformer.AbstractTransformer;

public class TrimStringTransformer extends AbstractTransformer {

    @Override
    public String transform(Object value) {
        if (value == null) {
            return null;
        } else {
            return ((String) value).trim(); // TODO: add generic
        }
    }

    @Override
    public DataType getInputDataType() {
        return DataType.STRING;
    }

    @Override
    public DataType getOutputDataType() {
        return DataType.STRING;
    }
}
