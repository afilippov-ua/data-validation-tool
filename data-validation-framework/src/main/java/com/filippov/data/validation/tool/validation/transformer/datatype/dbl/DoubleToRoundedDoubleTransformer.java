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

package com.filippov.data.validation.tool.validation.transformer.datatype.dbl;

import com.filippov.data.validation.tool.model.DataType;
import com.filippov.data.validation.tool.validation.transformer.AbstractTransformer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
public class DoubleToRoundedDoubleTransformer extends AbstractTransformer {

    @NonNull
    private final RoundingMode roundingMode;
    private final int places;

    @Override
    public Object transform(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Double) {
            return roundDouble((Double) value);
        }
        throw new IllegalArgumentException("Value: '" + value + "' is not a Double value!");
    }

    @Override
    public DataType getInputDataType() {
        return DataType.DOUBLE;
    }

    @Override
    public DataType getOutputDataType() {
        return DataType.DOUBLE;
    }

    private double roundDouble(Double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, roundingMode);
        return bd.doubleValue();
    }
}
