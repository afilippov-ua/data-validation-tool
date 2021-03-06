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

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class DatasourceMetadataTest extends AbstractUnitTest {

    private final DatasourceMetadata metadata = DatasourceMetadata.builder()
            .tables(asList(USERS_TABLE, DEPARTMENTS_TABLE))
            .columns(asList(
                    USERS_ID_COLUMN, USERS_USERNAME_COLUMN, USERS_PASSWORD_COLUMN,
                    DEPARTMENTS_ID_COLUMN, DEPARTMENTS_NAME_COLUMN, DEPARTMENTS_NUMBER_OF_EMPLOYEES_COLUMN))
            .build();

    static Object[][] tableNameProvider() {
        return new Object[][]{
                {USERS, USERS_TABLE},
                {DEPARTMENTS, DEPARTMENTS_TABLE}
        };
    }

    static Object[][] columnNameProvider() {
        return new Object[][]{
                {USERS, USERS_ID, USERS_ID_COLUMN},
                {USERS, USERS_ID, USERS_ID_COLUMN},
                {USERS, USERS_ID, USERS_ID_COLUMN},
                {DEPARTMENTS, DEPARTMENTS_ID, DEPARTMENTS_ID_COLUMN},
                {DEPARTMENTS, DEPARTMENTS_NAME, DEPARTMENTS_NAME_COLUMN},
                {DEPARTMENTS, DEPARTMENTS_NUMBER_OF_EMPLOYEES, DEPARTMENTS_NUMBER_OF_EMPLOYEES_COLUMN}
        };
    }

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, new ArrayList<>()},
                {new ArrayList<>(), null}
        };
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void incorrectInputTest(List<DatasourceTable> tables, List<DatasourceColumn> columns) {
        Assertions.assertThatThrownBy(() -> new DatasourceMetadata(tables, columns))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest()
    @MethodSource("tableNameProvider")
    void getTableByNameTest(String tableName, DatasourceTable expectedTable) {
        assertThat(metadata.getTableByName(tableName)).isEqualTo(expectedTable);
    }

    @Test
    void incorrectTableNameTest() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> metadata.getTableByName("incorrect name"))
                .withNoCause();
    }

    @ParameterizedTest()
    @MethodSource("columnNameProvider")
    void getColumnByNameTest(String tableName, String columnName, DatasourceColumn expectedColumn) {
        assertThat(metadata.getColumnByName(tableName, columnName)).isEqualTo(expectedColumn);
    }

    @Test
    void incorrectColumnsNameTest() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> metadata.getColumnByName(USERS, "incorrect name"))
                .withNoCause();
    }
}
