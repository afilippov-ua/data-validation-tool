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

package com.filippov.data.validation.tool.datasource;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.model.DataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DatasourceMetadataTest extends AbstractTest {
    private static final DatasourceTable table1 = DatasourceTable.builder().name(TABLE_A).build();
    private static final DatasourceTable table2 = DatasourceTable.builder().name(TABLE_B).build();

    private static final DatasourceColumn column1 = DatasourceColumn.builder().tableName(TABLE_A).name(INTEGER_COLUMN).dataType(DataType.INTEGER).build();
    private static final DatasourceColumn column2 = DatasourceColumn.builder().tableName(TABLE_A).name(DOUBLE_COLUMN).dataType(DataType.DOUBLE).build();
    private static final DatasourceColumn column3 = DatasourceColumn.builder().tableName(TABLE_B).name(INTEGER_COLUMN).dataType(DataType.INTEGER).build();
    private static final DatasourceColumn column4 = DatasourceColumn.builder().tableName(TABLE_B).name(DOUBLE_COLUMN).dataType(DataType.DOUBLE).build();

    private static final DatasourceMetadata metadata = DatasourceMetadata.builder()
            .tables(asList(table1, table2))
            .columns(asList(column1, column2, column3, column4))
            .build();

    static Object[][] tableNameProvider() {
        return new Object[][]{
                {TABLE_A, table1},
                {TABLE_B, table2}
        };
    }

    static Object[][] columnNameProvider() {
        return new Object[][]{
                {TABLE_A, INTEGER_COLUMN, column1},
                {TABLE_A, DOUBLE_COLUMN, column2},
                {TABLE_B, INTEGER_COLUMN, column3},
                {TABLE_B, DOUBLE_COLUMN, column4}
        };
    }

    @ParameterizedTest()
    @MethodSource("tableNameProvider")
    void getTableByNameTest(String columnName, DatasourceTable expectedTable) {
        assertThat(metadata.getTableByName(columnName)).isEqualTo(expectedTable);
    }

    @Test
    void incorrectTableNameTest() {
        assertThatExceptionOfType(IllegalArgumentException.class)
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
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> metadata.getColumnByName(TABLE_A, "incorrect name"))
                .withNoCause();
    }
}
