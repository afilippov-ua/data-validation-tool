package com.filippov.data.validation.tool.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatasourceTableTest {
    private static final DatasourceColumn PK = DatasourceColumn.builder().name("pk").build();
    private static final DatasourceColumn COLUMN1 = DatasourceColumn.builder().name("column1").build();
    private static final DatasourceColumn COLUMN2 = DatasourceColumn.builder().name("column2").build();

    private static final DatasourceTable TABLE = DatasourceTable.builder()
            .name("table1")
            .primaryKey(PK)
            .columns(asList(PK, COLUMN1, COLUMN2))
            .build();

    static Object[][] columnNameProvider() {
        return new Object[][]{
                {"column1", COLUMN1},
                {"column2", COLUMN2}
        };
    }

    @ParameterizedTest()
    @MethodSource("columnNameProvider")
    void getTableByNameTest(String columnName, DatasourceColumn expectedColumn) {
        assertThat(TABLE.getColumnByName(columnName)).isEqualTo(expectedColumn);
    }

    @Test
    void incorrectTableNameTest() {
        assertThrows(IllegalArgumentException.class, () -> TABLE.getColumnByName("incorrect name"));
    }
}
