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

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ObjectToBooleanTransformerTest {

    static Object[][] valueProvider() {
        return new Object[][]{
                {asSet(true), asSet(false), asSet((Boolean) null), true, true},
                {asSet(true), asSet(false), asSet((Boolean) null), false, false},
                {asSet(true), asSet(false), asSet((Boolean) null), null, null},

                {asSet("true"), asSet("false"), asSet("null"), "true", true},
                {asSet("true"), asSet("false"), asSet("null"), "false", false},
                {asSet("true"), asSet("false"), asSet("null"), "null", null},
                {asSet("true"), asSet("false"), asSet("null", null), null, null},

                {asSet(0), asSet(1), asSet(-1), 0, true},
                {asSet(0), asSet(1), asSet(-1), 1, false},
                {asSet(0), asSet(1), asSet(-1), -1, null},
                {asSet(0), asSet(1), asSet(-1, null), null, null},

                {asSet(true, "true", 0), asSet(false, "false", 1), asSet(null, "null", -1), true, true},
                {asSet(true, "true", 0), asSet(false, "false", 1), asSet(null, "null", -1), "true", true},
                {asSet(true, "true", 0), asSet(false, "false", 1), asSet(null, "null", -1), 0, true},
                {asSet(true, "true", 0), asSet(false, "false", 1), asSet(null, "null", -1), false, false},
                {asSet(true, "true", 0), asSet(false, "false", 1), asSet(null, "null", -1), "false", false},
                {asSet(true, "true", 0), asSet(false, "false", 1), asSet(null, "null", -1), 1, false},
                {asSet(true, "true", 0), asSet(false, "false", -1), asSet(null, "null", -1), null, null},
                {asSet(true, "true", 0), asSet(false, "false", -1), asSet(null, "null", -1), "null", null},
                {asSet(true, "true", 0), asSet(false, "false", -1), asSet(null, "null", -1), -1, null}
        };
    }

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, asSet(false), asSet((Boolean) null)},
                {asSet(true), null, asSet((Boolean) null)},
                {asSet(true), asSet(false), null}
        };
    }

    private static Set<Object> asSet(Object... values) {
        final Set<Object> result = new HashSet<>();
        if (values == null) {
            result.add(null);
        } else {
            for (Object val : values) {
                result.add(val);
            }
        }
        return result;
    }

    @ParameterizedTest()
    @MethodSource("valueProvider")
    void transformerTest(Set<Object> trueValues, Set<Object> falseValues, Set<Object> nullValues, Object inputValue, Boolean expectedValue) {
        final ObjectToBooleanTransformer transformer = new ObjectToBooleanTransformer(trueValues, falseValues, nullValues);
        assertThat(transformer.transform(inputValue)).isEqualTo(expectedValue);
    }

    @ParameterizedTest()
    @MethodSource("incorrectInputProvider")
    void incorrectInputMustThrowAnException(Set<Object> trueValues, Set<Object> falseValues, Set<Object> nullValues) {
        assertThatThrownBy(() -> new ObjectToBooleanTransformer(trueValues, falseValues, nullValues))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void incorrectValueTransformationMustThrowAnException() {
        final ObjectToBooleanTransformer transformer = new ObjectToBooleanTransformer(asSet("true"), asSet("false"), asSet("null"));
        assertThatThrownBy(() -> transformer.transform("incorrect_value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Value")
                .hasMessageContaining("cannot be converted to boolean");
    }

    @Test
    void getInputDataTypeTest() {
        assertThat(new ObjectToBooleanTransformer(asSet("true"), asSet("false"), asSet("null")).getInputDataType())
                .isNotNull()
                .isEqualTo(DataType.OBJECT);
    }

    @Test
    void getOutputDataTypeTest() {
        assertThat(new ObjectToBooleanTransformer(asSet("true"), asSet("false"), asSet("null")).getOutputDataType())
                .isNotNull()
                .isEqualTo(DataType.BOOLEAN);
    }
}
