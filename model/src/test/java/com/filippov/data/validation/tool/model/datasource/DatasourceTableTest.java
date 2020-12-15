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

public class DatasourceTableTest extends AbstractUnitTest {

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
        Assertions.assertThatThrownBy(() -> new DatasourceTable(name, primaryKey, columns))
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
