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

package com.filippov.data.validation.tool.pair;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TablePairTest extends AbstractTest {

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, USERS_TABLE_PAIR.getName(), USERS_TABLE_PAIR.getKeyColumnPair(),
                        USERS_TABLE_PAIR.getLeftDatasourceTable(), USERS_TABLE_PAIR.getRightDatasourceTable()},
                {USERS_TABLE_PAIR.getId(), null, USERS_TABLE_PAIR.getKeyColumnPair(),
                        USERS_TABLE_PAIR.getLeftDatasourceTable(), USERS_TABLE_PAIR.getRightDatasourceTable()},
                {USERS_TABLE_PAIR.getId(), USERS_TABLE_PAIR.getName(), USERS_TABLE_PAIR.getKeyColumnPair(),
                        null, USERS_TABLE_PAIR.getRightDatasourceTable()},
                {USERS_TABLE_PAIR.getId(), USERS_TABLE_PAIR.getName(), USERS_TABLE_PAIR.getKeyColumnPair(),
                        USERS_TABLE_PAIR.getLeftDatasourceTable(), null}
        };
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(String id, String name, ColumnPair keyColumnPair,
                                                      DatasourceTable leftDatasourceTable, DatasourceTable rightDatasourceTable) {
        assertThatThrownBy(() -> new TablePair(id, name, keyColumnPair, leftDatasourceTable, rightDatasourceTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void columnDataPairConstructorTest() {
        final TablePair tablePair = new TablePair(USERS_TABLE_PAIR.getId(), USERS_TABLE_PAIR.getName(), USERS_TABLE_PAIR.getKeyColumnPair(),
                USERS_TABLE_PAIR.getLeftDatasourceTable(), USERS_TABLE_PAIR.getRightDatasourceTable());

        assertThat(tablePair).isNotNull();
        assertThat(tablePair.getId()).isNotNull().isEqualTo(USERS_TABLE_PAIR.getId());
        assertThat(tablePair.getName()).isNotNull().isEqualTo(USERS_TABLE_PAIR.getName());
        assertThat(tablePair.getKeyColumnPair()).isNotNull().isEqualTo(USERS_TABLE_PAIR.getKeyColumnPair());
        assertThat(tablePair.getLeftDatasourceTable()).isNotNull().isEqualTo(USERS_TABLE_PAIR.getLeftDatasourceTable());
        assertThat(tablePair.getRightDatasourceTable()).isNotNull().isEqualTo(USERS_TABLE_PAIR.getRightDatasourceTable());
    }

    @Test
    void columnDataPairBuilderTest() {
        final TablePair tablePair = TablePair.builder()
                .id(USERS_TABLE_PAIR.getId())
                .name(USERS_TABLE_PAIR.getName())
                .keyColumnPair(USERS_TABLE_PAIR.getKeyColumnPair())
                .leftDatasourceTable(USERS_TABLE_PAIR.getLeftDatasourceTable())
                .rightDatasourceTable(USERS_TABLE_PAIR.getRightDatasourceTable())
                .build();

        assertThat(tablePair).isNotNull();
        assertThat(tablePair.getId()).isNotNull().isEqualTo(USERS_TABLE_PAIR.getId());
        assertThat(tablePair.getName()).isNotNull().isEqualTo(USERS_TABLE_PAIR.getName());
        assertThat(tablePair.getKeyColumnPair()).isNotNull().isEqualTo(USERS_TABLE_PAIR.getKeyColumnPair());
        assertThat(tablePair.getLeftDatasourceTable()).isNotNull().isEqualTo(USERS_TABLE_PAIR.getLeftDatasourceTable());
        assertThat(tablePair.getRightDatasourceTable()).isNotNull().isEqualTo(USERS_TABLE_PAIR.getRightDatasourceTable());
    }
}
