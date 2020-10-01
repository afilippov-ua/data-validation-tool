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

package com.filippov.data.validation.tool.validation.transformer.datatype.str;

import com.filippov.data.validation.tool.model.DataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StringToListSplitByRegexTransformerTest {

    static Object[][] valueProvider() {
        return new Object[][]{
                {"it is a good test", " ", asList("it", "is", "a", "good", "test")},
                {null, "", null}
        };
    }

    @ParameterizedTest()
    @MethodSource("valueProvider")
    void transformerTest(String src, String regexp, List<String> expectedValue) {
        final StringToListSplitByRegexTransformer transformer = new StringToListSplitByRegexTransformer(regexp);
        assertThat(transformer.transform(src)).isEqualTo(expectedValue);
    }

    @Test
    void incorrectDataTypeTransformationMustThrowAnException() {
        final StringToListSplitByRegexTransformer transformer = new StringToListSplitByRegexTransformer("");
        assertThatThrownBy(() -> transformer.transform(Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported data type");
    }

    @Test
    void getInputDataTypeTest() {
        assertThat(new StringToListSplitByRegexTransformer("").getInputDataType())
                .isNotNull()
                .isEqualTo(DataType.STRING);
    }

    @Test
    void getOutputDataTypeTest() {
        assertThat(new StringToListSplitByRegexTransformer("").getOutputDataType())
                .isNotNull()
                .isEqualTo(DataType.LIST);
    }
}
