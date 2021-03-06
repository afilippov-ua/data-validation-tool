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

public class ObjectToDoubleTransformer extends AbstractTransformer<Object, Double> {

    @Override
    public Double transform(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Value: " + value + " cannot be parsed to double!", ex);
            }
        } else if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + value.getClass().getSimpleName());
        }
    }

    @Override
    public DataType getInputDataType() {
        return DataType.OBJECT;
    }

    @Override
    public DataType getOutputDataType() {
        return DataType.DOUBLE;
    }
}
