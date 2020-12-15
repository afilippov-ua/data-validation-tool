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

package com.filippov.data.validation.tool.validation.transformer.datatype.lst;

import com.filippov.data.validation.tool.model.datasource.DataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class ToListTransformerTest {

    static Object[][] valueProvider() {
        return new Object[][]{
                {asList("string1", "string2"), asList("string1", "string2")},
                {asList(1, 2), asList(1, 2)},
                {asList(1.0, 2.0), asList(1.0, 2.0)},
                {null, null}
        };
    }

    @ParameterizedTest()
    @MethodSource("valueProvider")
    void transformerTest(Object value, List<Object> expectedList) {
        final ToListTransformer transformer = new ToListTransformer();
        assertThat(transformer.transform(value)).isEqualTo(expectedList);
    }

    @Test
    void getInputDataTypeTest() {
        assertThat(new ToListTransformer().getInputDataType())
                .isNotNull()
                .isEqualTo(DataType.OBJECT);
    }

    @Test
    void getOutputDataTypeTest() {
        assertThat(new ToListTransformer().getOutputDataType())
                .isNotNull()
                .isEqualTo(DataType.LIST);
    }
}
