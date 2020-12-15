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

package com.filippov.data.validation.tool.validation.transformer.datatype.obj;

import com.filippov.data.validation.tool.model.datasource.DataType;
import com.filippov.data.validation.tool.validation.transformer.AbstractTransformer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class ObjectToBooleanTransformer extends AbstractTransformer<Object, Boolean> {
    @NonNull
    private final Set<Object> trueValues;
    @NonNull
    private final Set<Object> falseValues;
    @NonNull
    private final Set<Object> nullValues;

    @Override
    public Boolean transform(Object value) {
        if (nullValues.contains(value)) {
            return null;
        } else if (trueValues.contains(value)) {
            return true;
        } else if (falseValues.contains(value)) {
            return false;
        }
        throw new IllegalArgumentException("Value: '" + value + "' cannot be converted to boolean!");
    }

    @Override
    public DataType getInputDataType() {
        return DataType.OBJECT;
    }

    @Override
    public DataType getOutputDataType() {
        return DataType.BOOLEAN;
    }
}
