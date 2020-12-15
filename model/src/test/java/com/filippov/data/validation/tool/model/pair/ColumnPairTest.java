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

package com.filippov.data.validation.tool.model.pair;

import com.filippov.data.validation.tool.AbstractUnitTest;
import com.filippov.data.validation.tool.model.Transformer;
import com.filippov.data.validation.tool.model.datasource.DatasourceColumn;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ColumnPairTest extends AbstractUnitTest {

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, USERS_USERNAME_COLUMN_PAIR.getId(), USERS_USERNAME_COLUMN_PAIR.getName(),
                        USERS_USERNAME_COLUMN_PAIR.getLeftDatasourceColumn(), USERS_USERNAME_COLUMN_PAIR.getRightDatasourceColumn(),
                        USERS_USERNAME_COLUMN_PAIR.getLeftTransformer(), USERS_USERNAME_COLUMN_PAIR.getRightTransformer()},
                {USERS_TABLE_PAIR, null, USERS_USERNAME_COLUMN_PAIR.getName(),
                        USERS_USERNAME_COLUMN_PAIR.getLeftDatasourceColumn(), USERS_USERNAME_COLUMN_PAIR.getRightDatasourceColumn(),
                        USERS_USERNAME_COLUMN_PAIR.getLeftTransformer(), USERS_USERNAME_COLUMN_PAIR.getRightTransformer()},
                {USERS_TABLE_PAIR, USERS_USERNAME_COLUMN_PAIR.getId(), null,
                        USERS_USERNAME_COLUMN_PAIR.getLeftDatasourceColumn(), USERS_USERNAME_COLUMN_PAIR.getRightDatasourceColumn(),
                        USERS_USERNAME_COLUMN_PAIR.getLeftTransformer(), USERS_USERNAME_COLUMN_PAIR.getRightTransformer()},
                {USERS_TABLE_PAIR, USERS_USERNAME_COLUMN_PAIR.getId(), USERS_USERNAME_COLUMN_PAIR.getName(),
                        null, USERS_USERNAME_COLUMN_PAIR.getRightDatasourceColumn(),
                        USERS_USERNAME_COLUMN_PAIR.getLeftTransformer(), USERS_USERNAME_COLUMN_PAIR.getRightTransformer()},
                {USERS_TABLE_PAIR, USERS_USERNAME_COLUMN_PAIR.getId(), USERS_USERNAME_COLUMN_PAIR.getName(),
                        USERS_USERNAME_COLUMN_PAIR.getLeftDatasourceColumn(), null,
                        USERS_USERNAME_COLUMN_PAIR.getLeftTransformer(), USERS_USERNAME_COLUMN_PAIR.getRightTransformer()},
                {USERS_TABLE_PAIR, USERS_USERNAME_COLUMN_PAIR.getId(), USERS_USERNAME_COLUMN_PAIR.getName(),
                        USERS_USERNAME_COLUMN_PAIR.getLeftDatasourceColumn(), USERS_USERNAME_COLUMN_PAIR.getRightDatasourceColumn(),
                        null, USERS_USERNAME_COLUMN_PAIR.getRightTransformer()},
                {USERS_TABLE_PAIR, USERS_USERNAME_COLUMN_PAIR.getId(), USERS_USERNAME_COLUMN_PAIR.getName(),
                        USERS_USERNAME_COLUMN_PAIR.getLeftDatasourceColumn(), USERS_USERNAME_COLUMN_PAIR.getRightDatasourceColumn(),
                        USERS_USERNAME_COLUMN_PAIR.getLeftTransformer(), null}
        };
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(TablePair tablePair, String id, String name,
                                                      DatasourceColumn leftDatasourceColumn, DatasourceColumn rightDatasourceColumn,
                                                      Transformer leftTransformer, Transformer rightTransformer) {
        Assertions.assertThatThrownBy(() -> new ColumnPair(id, name, tablePair, leftDatasourceColumn, rightDatasourceColumn, leftTransformer, rightTransformer))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void columnPairConstructorTest() {
        final ColumnPair columnPair = new ColumnPair(USERS_USERNAME_COLUMN_PAIR.getId(), USERS_USERNAME_COLUMN_PAIR.getName(), USERS_TABLE_PAIR,
                USERS_USERNAME_COLUMN_PAIR.getLeftDatasourceColumn(), USERS_USERNAME_COLUMN_PAIR.getRightDatasourceColumn(),
                USERS_USERNAME_COLUMN_PAIR.getLeftTransformer(), USERS_USERNAME_COLUMN_PAIR.getRightTransformer());

        assertThat(columnPair).isNotNull();
        assertThat(columnPair.getId()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR.getId());
        assertThat(columnPair.getName()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR.getName());
        assertThat(columnPair.getTablePair()).isNotNull().isEqualTo(USERS_TABLE_PAIR);
        assertThat(columnPair.getLeftDatasourceColumn()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR.getLeftDatasourceColumn());
        assertThat(columnPair.getRightDatasourceColumn()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR.getRightDatasourceColumn());
        assertThat(columnPair.getLeftTransformer()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR.getLeftTransformer());
        assertThat(columnPair.getRightTransformer()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR.getRightTransformer());
    }

    @Test
    void columnPairBuilderTest() {
        final ColumnPair columnPair = ColumnPair.builder()
                .id(USERS_USERNAME_COLUMN_PAIR.getId())
                .name(USERS_USERNAME_COLUMN_PAIR.getName())
                .tablePair(USERS_TABLE_PAIR)
                .leftDatasourceColumn(USERS_USERNAME_COLUMN_PAIR.getLeftDatasourceColumn())
                .rightDatasourceColumn(USERS_USERNAME_COLUMN_PAIR.getRightDatasourceColumn())
                .leftTransformer(USERS_USERNAME_COLUMN_PAIR.getLeftTransformer())
                .rightTransformer(USERS_USERNAME_COLUMN_PAIR.getRightTransformer())
                .build();

        assertThat(columnPair).isNotNull();
        assertThat(columnPair.getId()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR.getId());
        assertThat(columnPair.getName()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR.getName());
        assertThat(columnPair.getTablePair()).isNotNull().isEqualTo(USERS_TABLE_PAIR);
        assertThat(columnPair.getLeftDatasourceColumn()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR.getLeftDatasourceColumn());
        assertThat(columnPair.getRightDatasourceColumn()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR.getRightDatasourceColumn());
        assertThat(columnPair.getLeftTransformer()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR.getLeftTransformer());
        assertThat(columnPair.getRightTransformer()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR.getRightTransformer());
    }
}
