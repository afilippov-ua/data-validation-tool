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

package com.filippov.data.validation.tool.binder;

import com.filippov.data.validation.tool.AbstractUnitTest;
import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.dto.validation.DataRowDto;
import com.filippov.data.validation.tool.pair.ColumnDataPair;
import com.filippov.data.validation.tool.pair.ColumnPair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DefaultDataBinderTest extends AbstractUnitTest {

    private static final ColumnDataPair<Object, Object, Object> COLUMN_DATA_PAIR = STORAGE_PAIR.getColumnData(Query.builder()
            .tablePair(USERS_TABLE_PAIR)
            .columnPair(USERS_USERNAME_COLUMN_PAIR)
            .build());

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, USERS_USERNAME_COLUMN_PAIR, 1},
                {COLUMN_DATA_PAIR, null, 1},
                {COLUMN_DATA_PAIR, USERS_USERNAME_COLUMN_PAIR, null}
        };
    }

    @Test
    void defaultDataBinderBindTest() {
        final DefaultDataBinder binder = new DefaultDataBinder();
        final DataRowDto rowDto = binder.bind(COLUMN_DATA_PAIR, USERS_USERNAME_COLUMN_PAIR, 2);

        assertThat(rowDto).isNotNull();
        assertThat(rowDto.getKey()).isNotNull().isEqualTo(2);
        assertThat(rowDto.getLeftOriginalValue()).isNotNull().isEqualTo("user2");
        assertThat(rowDto.getRightOriginalValue()).isNotNull().isEqualTo("user2_changed");
        assertThat(rowDto.getLeftTransformedValue()).isNotNull().isEqualTo("user2");
        assertThat(rowDto.getRightTransformedValue()).isNotNull().isEqualTo("user2_changed");
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(ColumnDataPair<Object, Object, Object> columnDataPair, ColumnPair columnPair, Object key) {
        assertThatThrownBy(() -> new DefaultDataBinder().bind(columnDataPair, columnPair, key))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
