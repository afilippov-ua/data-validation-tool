package com.filippov.data.validation.tool.validation.transformer;

import com.filippov.data.validation.tool.model.DataType;

public interface Transformer {
    Object transform(Object value);

    Transformer getNext();

    void setNext(Transformer nextTransformer);

    Transformer getLastTransformer();

    DataType getInputDataType();

    DataType getOutputDataType();

}
