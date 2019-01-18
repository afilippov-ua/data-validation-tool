package com.filippov.data.validation.tool.datasource;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.model.DataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(metadata.getTableByName(columnName)).hasValue(expectedTable);
    }

    @Test
    void incorrectTableNameTest() {
        assertThat(metadata.getTableByName("incorrect name")).isEmpty();
    }

    @ParameterizedTest()
    @MethodSource("columnNameProvider")
    void getColumnByNameTest(String tableName, String columnName, DatasourceColumn expectedColumn) {
        assertThat(metadata.getColumnByName(tableName, columnName)).hasValue(expectedColumn);
    }

    @Test
    void incorrectColumnsNameTest() {
        assertThat(metadata.getColumnByName(TABLE_A, "incorrect name")).isEmpty();
    }
}
