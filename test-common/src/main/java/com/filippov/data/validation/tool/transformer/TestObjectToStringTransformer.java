/*
 *   Copyright 2018-2020 the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.filippov.data.validation.tool.transformer;

import com.filippov.data.validation.tool.model.Transformer;
import com.filippov.data.validation.tool.model.datasource.DataType;

import java.util.Objects;

public class TestObjectToStringTransformer implements Transformer<Object, String> {
    private Transformer<String, ?> next;

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

    @Override
    public Transformer<String, ?> getNext() {
        return next;
    }

    @Override
    public void setNext(Transformer<String, ?> nextTransformer) {
        this.next = nextTransformer;
    }

    @Override
    public Transformer<?, ?> getLastTransformer() {
        if (next == null) {
            return this;
        } else {
            return next.getLastTransformer();
        }
    }

    @Override
    public DataType getInputDataType() {
        return DataType.OBJECT;
    }

    @Override
    public DataType getOutputDataType() {
        return DataType.STRING;
    }
}
