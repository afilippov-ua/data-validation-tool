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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DatasourceColumnTest {

    private static final String TABLE_NAME = "test-table-name";
    private static final String NAME = "test-table-name";

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, NAME, DataType.DOUBLE},
                {"", NAME, DataType.DOUBLE},
                {TABLE_NAME, null, DataType.DOUBLE},
                {TABLE_NAME, "", DataType.DOUBLE},
                {TABLE_NAME, NAME, null}
        };
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(String tableName, String name, DataType dataType) {
        Assertions.assertThatThrownBy(() -> new DatasourceColumn(tableName, name, dataType))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void datasourceColumnConstructorTest() {
        final DatasourceColumn datasourceColumn = new DatasourceColumn(TABLE_NAME, NAME, DataType.STRING);
        assertThat(datasourceColumn).isNotNull();
        assertThat(datasourceColumn.getTableName()).isEqualTo(TABLE_NAME);
        assertThat(datasourceColumn.getName()).isEqualTo(NAME);
        assertThat(datasourceColumn.getDataType()).isEqualTo(DataType.STRING);
    }
}
