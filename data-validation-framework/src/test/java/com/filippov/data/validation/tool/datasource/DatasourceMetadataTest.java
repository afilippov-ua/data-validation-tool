package com.filippov.data.validation.tool.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatasourceMetadataTest {
    private static final DatasourceTable table1 = DatasourceTable.builder().name("table1").build();
    private static final DatasourceTable table2 = DatasourceTable.builder().name("table2").build();

    private static final DatasourceMetadata metadata = DatasourceMetadata.builder()
            .tables(asList(table1, table2))
            .build();

    static Object[][] tableNameProvider() {
        return new Object[][]{
                {"table1", table1},
                {"table2", table2}
        };
    }

    @ParameterizedTest()
    @MethodSource("tableNameProvider")
    void getTableByNameTest(String columnName, DatasourceTable expectedTable) {
        assertThat(metadata.getTableByName(columnName)).isEqualTo(expectedTable);
    }

    @Test
    void incorrectTableNameTest() {
        assertThrows(IllegalArgumentException.class, () -> metadata.getTableByName("incorrect name"));
    }
}
