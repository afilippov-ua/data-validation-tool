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
import com.filippov.data.validation.tool.model.ColumnDataInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ColumnDataInfoPairTest extends AbstractTest {

    private static final ColumnDataInfo LEFT_INFO = ColumnDataInfo.builder()
            .cached(true)
            .date(Instant.now())
            .build();
    private static final ColumnDataInfo RIGHT_INFO = ColumnDataInfo.builder()
            .cached(true)
            .date(Instant.now())
            .build();

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, USERS_USERNAME_COLUMN_PAIR, LEFT_INFO, RIGHT_INFO},
                {USERS_TABLE_PAIR, null, LEFT_INFO, RIGHT_INFO},
                {USERS_TABLE_PAIR, USERS_USERNAME_COLUMN_PAIR, null, RIGHT_INFO},
                {USERS_TABLE_PAIR, USERS_USERNAME_COLUMN_PAIR, LEFT_INFO, null}
        };
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(TablePair tablePair, ColumnPair columnPair, ColumnDataInfo leftInfo, ColumnDataInfo rightInfo) {
        assertThatThrownBy(() -> new ColumnDataInfoPair(tablePair, columnPair, leftInfo, rightInfo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void columnDataInfoPairConstructorTest() {
        final ColumnDataInfoPair columnDataInfoPair = new ColumnDataInfoPair(USERS_TABLE_PAIR, USERS_USERNAME_COLUMN_PAIR, LEFT_INFO, RIGHT_INFO);

        assertThat(columnDataInfoPair).isNotNull();
        assertThat(columnDataInfoPair.getTablePair()).isNotNull().isEqualTo(USERS_TABLE_PAIR);
        assertThat(columnDataInfoPair.getColumnPair()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR);
        assertThat(columnDataInfoPair.getLeftColumnDataInfo()).isNotNull().isEqualTo(LEFT_INFO);
        assertThat(columnDataInfoPair.getRightColumnDataInfo()).isNotNull().isEqualTo(RIGHT_INFO);
    }

    @Test
    void columnDataInfoPairBuilderTest() {
        final ColumnDataInfoPair columnDataInfoPair = ColumnDataInfoPair.builder()
                .tablePair(USERS_TABLE_PAIR)
                .columnPair(USERS_USERNAME_COLUMN_PAIR)
                .leftColumnDataInfo(LEFT_INFO)
                .rightColumnDataInfo(RIGHT_INFO)
                .build();

        assertThat(columnDataInfoPair).isNotNull();
        assertThat(columnDataInfoPair.getTablePair()).isNotNull().isEqualTo(USERS_TABLE_PAIR);
        assertThat(columnDataInfoPair.getColumnPair()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR);
        assertThat(columnDataInfoPair.getLeftColumnDataInfo()).isNotNull().isEqualTo(LEFT_INFO);
        assertThat(columnDataInfoPair.getRightColumnDataInfo()).isNotNull().isEqualTo(RIGHT_INFO);
    }
}
