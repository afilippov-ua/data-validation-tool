package com.filippov.data.validation.tool.datasource.model;

import com.filippov.data.validation.tool.model.DataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThatThrownBy(() -> new DatasourceColumn(tableName, name, dataType))
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
