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

package com.filippov.data.validation.tool.model.datasource;

import com.filippov.data.validation.tool.AbstractUnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

public class ColumnDataTest extends AbstractUnitTest {

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, USERS_USERNAME_COLUMN, emptyMap()},
                {USERS_ID_COLUMN, null, emptyMap()},
                {USERS_ID_COLUMN, USERS_USERNAME_COLUMN, null}
        };
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(DatasourceColumn keyColumn, DatasourceColumn dataColumn, Map<?, ?> dataMap) {
        Assertions.assertThatThrownBy(() -> new ColumnData<>(keyColumn, dataColumn, dataMap))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void columnDataConstructorTest() {
        final ColumnData<String, String> columnData = new ColumnData<>(USERS_ID_COLUMN, USERS_USERNAME_COLUMN, Map.of("1", "user1", "2", "user2"));
        assertThat(columnData).isNotNull();
        assertThat(columnData.getKeyColumn()).isNotNull().isEqualTo(USERS_ID_COLUMN);
        assertThat(columnData.getDataColumn()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN);
        assertThat(columnData.getKeys())
                .isNotNull()
                .isNotEmpty()
                .contains("1", "2");
        assertThat(columnData.getValueByKey("1")).isEqualTo("user1");
        assertThat(columnData.getValueByKey("2")).isEqualTo("user2");
    }

    @Test
    void columnDataBuilderTest() {
        final ColumnData<String, String> columnData = ColumnData.<String, String>builder()
                .keyColumn(USERS_ID_COLUMN)
                .dataColumn(USERS_USERNAME_COLUMN)
                .keys(asList("1", "2"))
                .data(asList("user1", "user2"))
                .build();

        assertThat(columnData).isNotNull();
        assertThat(columnData.getKeyColumn()).isNotNull().isEqualTo(USERS_ID_COLUMN);
        assertThat(columnData.getDataColumn()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN);
        assertThat(columnData.getKeys())
                .isNotNull()
                .isNotEmpty()
                .contains("1", "2");
        assertThat(columnData.getValueByKey("1")).isEqualTo("user1");
        assertThat(columnData.getValueByKey("2")).isEqualTo("user2");
    }
}
