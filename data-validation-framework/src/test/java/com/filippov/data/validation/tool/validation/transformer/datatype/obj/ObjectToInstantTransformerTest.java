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

public class ObjectToInstantTransformerTest {

    private static final Instant CURRENT_DATE = Instant.now();

    static Object[][] valueProvider() {
        return new Object[][]{
                {CURRENT_DATE, CURRENT_DATE},
                {CURRENT_DATE.toString(), CURRENT_DATE},
                {null, null}
        };
    }

    @ParameterizedTest()
    @MethodSource("valueProvider")
    void transformerTest(Object inputValue, Instant expectedValue) {
        final ObjectToInstantTransformer transformer = new ObjectToInstantTransformer();
        assertThat(transformer.transform(inputValue)).isEqualTo(expectedValue);
    }

    @Test
    void incorrectStringValueTransformationMustThrowAnException() {
        final ObjectToInstantTransformer transformer = new ObjectToInstantTransformer();
        assertThatThrownBy(() -> transformer.transform("incorrect_value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Value")
                .hasMessageContaining("cannot be converted to instant");
    }

    @Test
    void incorrectDataTypeTransformationMustThrowAnException() {
        final ObjectToInstantTransformer transformer = new ObjectToInstantTransformer();
        assertThatThrownBy(() -> transformer.transform(1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Value")
                .hasMessageContaining("cannot be converted to instant");
    }

    @Test
    void getInputDataTypeTest() {
        assertThat(new ObjectToInstantTransformer().getInputDataType())
                .isNotNull()
                .isEqualTo(DataType.OBJECT);
    }

    @Test
    void getOutputDataTypeTest() {
        assertThat(new ObjectToInstantTransformer().getOutputDataType())
                .isNotNull()
                .isEqualTo(DataType.INSTANT);
    }
}
