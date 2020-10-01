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

import com.filippov.data.validation.tool.model.DataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ObjectToIntegerTransformerTest {

    static Object[][] valueProvider() {
        return new Object[][]{
                {1, 1},
                {0, 0},
                {-1, -1},

                {"1", 1},
                {"0", 0},
                {"-1", -1},

                {1.67, 1},
                {0.0, 0},
                {-1.000001, -1},

                {null, null}
        };
    }

    @ParameterizedTest()
    @MethodSource("valueProvider")
    void transformerTest(Object value, Integer expectedValue) {
        final ObjectToIntegerTransformer transformer = new ObjectToIntegerTransformer();
        assertThat(transformer.transform(value)).isEqualTo(expectedValue);
    }

    @Test
    void incorrectStringValueTransformationMustThrowAnException() {
        final ObjectToIntegerTransformer transformer = new ObjectToIntegerTransformer();
        assertThatThrownBy(() -> transformer.transform("incorrect_value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Value")
                .hasMessageContaining("cannot be parsed to integer");
    }

    @Test
    void incorrectDataTypeTransformationMustThrowAnException() {
        final ObjectToIntegerTransformer transformer = new ObjectToIntegerTransformer();
        assertThatThrownBy(() -> transformer.transform(Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported data type");
    }

    @Test
    void getInputDataTypeTest() {
        assertThat(new ObjectToIntegerTransformer().getInputDataType())
                .isNotNull()
                .isEqualTo(DataType.OBJECT);
    }

    @Test
    void getOutputDataTypeTest() {
        assertThat(new ObjectToIntegerTransformer().getOutputDataType())
                .isNotNull()
                .isEqualTo(DataType.INTEGER);
    }
}
