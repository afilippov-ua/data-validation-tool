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

package com.filippov.data.validation.tool.databinder;

import com.filippov.data.validation.tool.AbstractUnitTest;
import com.filippov.data.validation.tool.model.Transformer;
import com.filippov.data.validation.tool.model.binder.DataRow;
import com.filippov.data.validation.tool.model.datasource.ColumnData;
import com.filippov.data.validation.tool.model.pair.ColumnDataPair;
import com.filippov.data.validation.tool.model.pair.ColumnPair;
import com.filippov.data.validation.tool.transformer.TestObjectToStringTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class DefaultDataBinderTest extends AbstractUnitTest {

    private static final ColumnDataPair<Object, Object, Object> COLUMN_DATA_PAIR = ColumnDataPair.builder()
            .leftColumnData(ColumnData.builder()
                    .keyColumn(USERS_ID_COLUMN)
                    .dataColumn(USERS_USERNAME_COLUMN)
                    .keys(asList(1, 2, 3))
                    .data(asList("u1", "u2", "u3"))
                    .build())
            .rightColumnData(ColumnData.builder()
                    .keyColumn(USERS_ID_COLUMN)
                    .dataColumn(USERS_USERNAME_COLUMN)
                    .keys(asList(1, 2, 3))
                    .data(asList("incorrect", "u2", "u3"))
                    .build())
            .build();

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, USERS_USERNAME_COLUMN_PAIR, 1},
                {COLUMN_DATA_PAIR, null, 1},
                {COLUMN_DATA_PAIR, USERS_USERNAME_COLUMN_PAIR, null}
        };
    }

    @Test
    void defaultDataBinderBindTest() {
        final Transformer<Object, String> transformer = Mockito.mock(Transformer.class);
        when(transformer.transform("u1")).thenReturn("user1");
        when(transformer.transform("u2")).thenReturn("user2");
        when(transformer.transform("u3")).thenReturn("user3");
        when(transformer.transform("incorrect")).thenReturn("incorrect-username");

        final ColumnPair columnPair = ColumnPair.builder()
                .id(UUID_GENERATOR.generateRandomUuid())
                .tablePair(USERS_TABLE_PAIR)
                .name(USERS_USERNAME)
                .leftDatasourceColumn(LEFT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_USERNAME))
                .rightDatasourceColumn(RIGHT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_USERNAME))
                .leftTransformer(transformer)
                .rightTransformer(transformer)
                .build();

        final DefaultDataBinder binder = new DefaultDataBinder();
        final DataRow row = binder.bind(COLUMN_DATA_PAIR, columnPair, 1);

        assertThat(row).isNotNull();
        assertThat(row.getKey()).isNotNull().isEqualTo(1);
        assertThat(row.getLeftOriginalValue()).isNotNull().isEqualTo("u1");
        assertThat(row.getRightOriginalValue()).isNotNull().isEqualTo("incorrect");
        assertThat(row.getLeftTransformedValue()).isNotNull().isEqualTo("user1");
        assertThat(row.getRightTransformedValue()).isNotNull().isEqualTo("incorrect-username");
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(ColumnDataPair<Object, Object, Object> columnDataPair, ColumnPair columnPair, Object key) {
        assertThatThrownBy(() -> new DefaultDataBinder().bind(columnDataPair, columnPair, key))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
