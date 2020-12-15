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

import static org.assertj.core.api.Assertions.assertThat;


public class DatasourceQueryTest extends AbstractUnitTest {

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, USERS_ID_COLUMN, USERS_USERNAME_COLUMN},
                {USERS_TABLE, null, USERS_USERNAME_COLUMN},
                {USERS_TABLE, USERS_ID_COLUMN, null},
        };
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(DatasourceTable datasourceTable, DatasourceColumn keyColumn, DatasourceColumn dataColumn) {
        Assertions.assertThatThrownBy(() -> new DatasourceQuery(datasourceTable, keyColumn, dataColumn))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void datasourceQueryConstructorTest() {
        final DatasourceQuery query = new DatasourceQuery(USERS_TABLE, USERS_ID_COLUMN, USERS_USERNAME_COLUMN);
        assertThat(query).isNotNull();
        assertThat(query.getTable()).isEqualTo(USERS_TABLE);
        assertThat(query.getKeyColumn()).isEqualTo(USERS_ID_COLUMN);
        assertThat(query.getDataColumn()).isEqualTo(USERS_USERNAME_COLUMN);
    }
}
