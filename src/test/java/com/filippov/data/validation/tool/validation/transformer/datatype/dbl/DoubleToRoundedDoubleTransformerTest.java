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

import com.filippov.data.validation.tool.model.datasource.DataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DoubleToRoundedDoubleTransformerTest {

    static Object[][] valueProvider() {
        return new Object[][]{
                {RoundingMode.HALF_UP, 2, 1.0, 1.0},
                {RoundingMode.HALF_UP, 2, 1.02, 1.02},
                {RoundingMode.HALF_UP, 2, 1.020, 1.02},
                {RoundingMode.HALF_UP, 2, 1.021, 1.02},
                {RoundingMode.HALF_UP, 2, 1.024, 1.02},
                {RoundingMode.HALF_UP, 2, 1.025, 1.02},
                {RoundingMode.HALF_UP, 2, 1.026, 1.03},
                {RoundingMode.HALF_UP, 2, 1.029, 1.03},
                {RoundingMode.HALF_UP, 2, -1.020, -1.02},
                {RoundingMode.HALF_UP, 2, -1.021, -1.02},
                {RoundingMode.HALF_UP, 2, -1.024, -1.02},
                {RoundingMode.HALF_UP, 2, -1.025, -1.02},
                {RoundingMode.HALF_UP, 2, -1.026, -1.03},
                {RoundingMode.HALF_UP, 2, -1.029, -1.03},
                {RoundingMode.HALF_UP, 2, null, null},

                {RoundingMode.HALF_UP, 0, 1.025, 1.0},
                {RoundingMode.HALF_UP, 0, -1.025, -1.0},

                {RoundingMode.HALF_UP, -1, 22.025, 20.0},
                {RoundingMode.HALF_UP, -1, 22.025, 20.0},
        };
    }

    @ParameterizedTest()
    @MethodSource("valueProvider")
    void transformerTest(RoundingMode roundingMode, Integer places, Double value, Double expectedValue) {
        final DoubleToRoundedDoubleTransformer transformer = new DoubleToRoundedDoubleTransformer(roundingMode, places);
        assertThat(transformer.transform(value)).isEqualTo(expectedValue);
    }

    @Test
    void incorrectInputMustThrowAnException() {
        assertThatThrownBy(() -> new DoubleToRoundedDoubleTransformer(null, 0))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void getInputDataTypeTest() {
        assertThat(new DoubleToRoundedDoubleTransformer(RoundingMode.HALF_UP, 2).getInputDataType())
                .isNotNull()
                .isEqualTo(DataType.DOUBLE);
    }

    @Test
    void getOutputDataTypeTest() {
        assertThat(new DoubleToRoundedDoubleTransformer(RoundingMode.HALF_UP, 2).getOutputDataType())
                .isNotNull()
                .isEqualTo(DataType.DOUBLE);
    }
}
