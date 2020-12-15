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

package com.filippov.data.validation.tool.model.metadata;

import com.filippov.data.validation.tool.AbstractUnitTest;
import com.filippov.data.validation.tool.model.pair.ColumnPair;
import com.filippov.data.validation.tool.model.pair.TablePair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;


public class MetadataTest extends AbstractUnitTest {
    private static final List<TablePair> TABLE_PAIRS = asList(USERS_TABLE_PAIR, DEPARTMENTS_TABLE_PAIR);
    private static final List<ColumnPair> COLUMN_PAIRS = asList(USERS_ID_COLUMN_PAIR, USERS_USERNAME_COLUMN_PAIR, USERS_PASSWORD_COLUMN_PAIR,
            DEPARTMENTS_ID_COLUMN_PAIR, DEPARTMENTS_NAME_COLUMN_PAIR, DEPARTMENTS_NUMBER_OF_EMPLOYEES_COLUMN_PAIR);

    static Object[][] tablePairProvider() {
        return new Object[][]{
                {USERS_TABLE_PAIR},
                {DEPARTMENTS_TABLE_PAIR}
        };
    }

    static Object[][] columnPairProvider() {
        return new Object[][]{
                {USERS_TABLE_PAIR, USERS_ID_COLUMN_PAIR},
                {USERS_TABLE_PAIR, USERS_USERNAME_COLUMN_PAIR},
                {USERS_TABLE_PAIR, USERS_PASSWORD_COLUMN_PAIR},
                {DEPARTMENTS_TABLE_PAIR, DEPARTMENTS_ID_COLUMN_PAIR},
                {DEPARTMENTS_TABLE_PAIR, DEPARTMENTS_NAME_COLUMN_PAIR},
                {DEPARTMENTS_TABLE_PAIR, DEPARTMENTS_NUMBER_OF_EMPLOYEES_COLUMN_PAIR}
        };
    }

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, emptyList()},
                {emptyList(), null},
        };
    }

    @Test
    void getTablePairsTest() {
        final Metadata metadata = Metadata.builder()
                .tablePairs(TABLE_PAIRS)
                .columnPairs(COLUMN_PAIRS)
                .build();

        assertThat(metadata.getTablePairs())
                .isNotNull()
                .isNotEmpty()
                .contains(USERS_TABLE_PAIR, DEPARTMENTS_TABLE_PAIR);
    }

    @ParameterizedTest()
    @MethodSource("tablePairProvider")
    void getTablePairByIdTest(TablePair tablePair) {
        final Metadata metadata = Metadata.builder()
                .tablePairs(TABLE_PAIRS)
                .columnPairs(COLUMN_PAIRS)
                .build();

        assertThat(metadata.getTablePairById(tablePair.getId()))
                .isNotNull()
                .isNotEmpty()
                .hasValue(tablePair);
    }

    @ParameterizedTest()
    @MethodSource("tablePairProvider")
    void getTablePairByNameTest(TablePair tablePair) {
        final Metadata metadata = Metadata.builder()
                .tablePairs(TABLE_PAIRS)
                .columnPairs(COLUMN_PAIRS)
                .build();

        assertThat(metadata.getTablePairByName(tablePair.getName()))
                .isNotNull()
                .isNotEmpty()
                .hasValue(tablePair);
    }

    @ParameterizedTest()
    @MethodSource("tablePairProvider")
    void getTablePairByIdOrNameTest(TablePair tablePair) {
        final Metadata metadata = Metadata.builder()
                .tablePairs(TABLE_PAIRS)
                .columnPairs(COLUMN_PAIRS)
                .build();

        assertThat(tablePair.getId()).isNotEqualTo(tablePair.getName());

        assertThat(metadata.getTablePairByIdOrName(tablePair.getId()))
                .isNotNull()
                .isNotEmpty()
                .hasValue(tablePair);
        assertThat(metadata.getTablePairByIdOrName(tablePair.getName()))
                .isNotNull()
                .isNotEmpty()
                .hasValue(tablePair);
    }

    @Test
    void getColumnPairsTest() {
        final Metadata metadata = Metadata.builder()
                .tablePairs(TABLE_PAIRS)
                .columnPairs(COLUMN_PAIRS)
                .build();

        assertThat(metadata.getColumnPairs(USERS_TABLE_PAIR))
                .isNotNull()
                .isNotEmpty()
                .contains(USERS_ID_COLUMN_PAIR, USERS_USERNAME_COLUMN_PAIR, USERS_PASSWORD_COLUMN_PAIR);
        assertThat(metadata.getColumnPairs(DEPARTMENTS_TABLE_PAIR))
                .isNotNull()
                .isNotEmpty()
                .contains(DEPARTMENTS_ID_COLUMN_PAIR, DEPARTMENTS_NAME_COLUMN_PAIR, DEPARTMENTS_NUMBER_OF_EMPLOYEES_COLUMN_PAIR);
    }

    @ParameterizedTest()
    @MethodSource("columnPairProvider")
    void getColumnPairByIdTest(TablePair tablePair, ColumnPair columnPair) {
        final Metadata metadata = Metadata.builder()
                .tablePairs(TABLE_PAIRS)
                .columnPairs(COLUMN_PAIRS)
                .build();

        assertThat(metadata.getColumnPairById(tablePair, columnPair.getId()))
                .isNotEmpty()
                .hasValue(columnPair);
    }

    @ParameterizedTest()
    @MethodSource("columnPairProvider")
    void getColumnPairByNameTest(TablePair tablePair, ColumnPair columnPair) {
        final Metadata metadata = Metadata.builder()
                .tablePairs(TABLE_PAIRS)
                .columnPairs(COLUMN_PAIRS)
                .build();

        assertThat(metadata.getColumnPairByName(tablePair, columnPair.getName()))
                .isNotEmpty()
                .hasValue(columnPair);
    }

    @ParameterizedTest()
    @MethodSource("columnPairProvider")
    void getColumnPairByIdOrNameTest(TablePair tablePair, ColumnPair columnPair) {
        final Metadata metadata = Metadata.builder()
                .tablePairs(TABLE_PAIRS)
                .columnPairs(COLUMN_PAIRS)
                .build();

        assertThat(columnPair.getId()).isNotEqualTo(columnPair.getName());

        assertThat(metadata.getColumnPairByIdOrName(tablePair, columnPair.getId()))
                .isNotNull()
                .isNotEmpty()
                .hasValue(columnPair);
        assertThat(metadata.getColumnPairByIdOrName(tablePair, columnPair.getName()))
                .isNotNull()
                .isNotEmpty()
                .hasValue(columnPair);
    }

    @ParameterizedTest()
    @MethodSource("incorrectInputProvider")
    void metadataConstructorIncorrectInputTest(List<TablePair> tablePairs, List<ColumnPair> columnPairs) {
        Assertions.assertThatThrownBy(() -> new Metadata(tablePairs, columnPairs))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
