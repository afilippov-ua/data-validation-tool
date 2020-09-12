package com.filippov.data.validation.tool.datasource.model;

import com.filippov.data.validation.tool.AbstractTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DatasourceTableTest extends AbstractTest {

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, USERS_ID, new ArrayList<>()},
                {"", USERS_ID, new ArrayList<>()},
                {USERS, null, new ArrayList<>()},
                {USERS, "", new ArrayList<>()},
                {USERS, USERS_ID, null}
        };
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(String name, String primaryKey, List<String> columns) {
        assertThatThrownBy(() -> new DatasourceTable(name, primaryKey, columns))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void datasourceTableConstructorTest() {
        final List<String> columns = asList(USERS_ID, USERS_USERNAME, USERS_PASSWORD);
        final DatasourceTable datasourceTable = new DatasourceTable(USERS, USERS_ID, columns);
        assertThat(datasourceTable).isNotNull();
        assertThat(datasourceTable.getName()).isEqualTo(USERS);
        assertThat(datasourceTable.getPrimaryKey()).isEqualTo(USERS_ID);
        assertThat(datasourceTable.getColumns()).isEqualTo(columns);
    }
}
