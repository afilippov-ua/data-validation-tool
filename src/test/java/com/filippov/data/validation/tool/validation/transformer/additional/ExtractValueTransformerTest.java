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

package com.filippov.data.validation.tool.validation.transformer.additional;

import com.filippov.data.validation.tool.model.datasource.DataType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ExtractValueTransformerTest {

    @Test
    void transformerTest() {
        final TestValue testValue = new TestValue(10, 20);
        final ExtractValueTransformer transformer = new ExtractValueTransformer((Function<TestValue, Integer>) TestValue::getA);
        assertThat(transformer.transform(testValue)).isEqualTo(10);
    }

    @Test
    void incorrectInputMustThrowAnException() {
        assertThatThrownBy(() -> new ExtractValueTransformer(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void getInputDataTypeTest() {
        assertThat(new ExtractValueTransformer((Function<String, Integer>) String::hashCode).getInputDataType())
                .isNotNull()
                .isEqualTo(DataType.OBJECT);
    }

    @Test
    void getOutputDataTypeTest() {
        assertThat(new ExtractValueTransformer((Function<String, Integer>) String::hashCode).getOutputDataType())
                .isNotNull()
                .isEqualTo(DataType.OBJECT);
    }

    @Getter
    @RequiredArgsConstructor
    private static class TestValue {
        private final int a;
        private final int b;
    }
}
