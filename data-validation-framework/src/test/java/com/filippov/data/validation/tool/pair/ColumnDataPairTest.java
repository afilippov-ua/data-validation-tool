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
import com.filippov.data.validation.tool.model.ColumnData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ColumnDataPairTest extends AbstractTest {

    private static final ColumnData<String, String> LEFT_COLUMN_DATA = ColumnData.<String, String>builder()
            .keyColumn(USERS_ID_COLUMN)
            .dataColumn(USERS_USERNAME_COLUMN)
            .keys(asList("1", "2"))
            .data(asList("user1", "user2"))
            .build();

    private static final ColumnData<String, String> RIGHT_COLUMN_DATA = ColumnData.<String, String>builder()
            .keyColumn(USERS_ID_COLUMN)
            .dataColumn(USERS_USERNAME_COLUMN)
            .keys(asList("1", "4"))
            .data(asList("user1", "user4"))
            .build();

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, RIGHT_COLUMN_DATA},
                {LEFT_COLUMN_DATA, null}
        };
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(ColumnData<String, String> leftData, ColumnData<String, String> rightData) {
        assertThatThrownBy(() -> new ColumnDataPair<>(leftData, rightData))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void columnDataPairConstructorTest() {
        final ColumnDataPair<String, String, String> columnDataPair = new ColumnDataPair<>(LEFT_COLUMN_DATA, RIGHT_COLUMN_DATA);
        assertThat(columnDataPair).isNotNull();
        assertThat(columnDataPair.getLeftColumnData()).isNotNull().isEqualTo(LEFT_COLUMN_DATA);
        assertThat(columnDataPair.getRightColumnData()).isNotNull().isEqualTo(RIGHT_COLUMN_DATA);
    }

    @Test
    void columnDataPairBuilderTest() {
        final ColumnDataPair<String, String, String> columnDataPair = ColumnDataPair.<String, String, String>builder()
                .leftColumnData(LEFT_COLUMN_DATA)
                .rightColumnData(RIGHT_COLUMN_DATA)
                .build();
        assertThat(columnDataPair).isNotNull();
        assertThat(columnDataPair.getLeftColumnData()).isNotNull().isEqualTo(LEFT_COLUMN_DATA);
        assertThat(columnDataPair.getRightColumnData()).isNotNull().isEqualTo(RIGHT_COLUMN_DATA);
    }
}
