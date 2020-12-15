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

package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.model.datastorage.DataStorageConfig;
import com.filippov.data.validation.tool.model.datastorage.RelationType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DataStorageConfigTest {

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, 1},
                {RelationType.LEFT, 0},
                {RelationType.LEFT, -1},
                {RelationType.LEFT, Integer.MIN_VALUE}
        };
    }

    static Object[][] correctInputProvider() {
        return new Object[][]{
                {RelationType.LEFT, 1},
                {RelationType.RIGHT, 100},
                {RelationType.LEFT, Integer.MAX_VALUE},
        };
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(RelationType relationType, Integer maxConnections) {
        assertThatThrownBy(() -> new DataStorageConfig(relationType, maxConnections))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("correctInputProvider")
    void datasourceColumnConstructorTest(RelationType relationType, Integer maxConnections) {
        final DataStorageConfig query = new DataStorageConfig(relationType, maxConnections);
        assertThat(query).isNotNull();
        assertThat(query.getRelationType()).isEqualTo(relationType);
        assertThat(query.getMaxConnections()).isEqualTo(maxConnections);
    }
}
