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

package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QueryTest extends AbstractTest {

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, USERS_USERNAME_COLUMN_PAIR},
                {USERS_TABLE_PAIR, null}
        };
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(TablePair tablePair, ColumnPair columnPair) {
        assertThatThrownBy(() -> new Query(tablePair, columnPair))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void datasourceQueryConstructorTest() {
        final Query query = new Query(USERS_TABLE_PAIR, USERS_USERNAME_COLUMN_PAIR);
        assertThat(query).isNotNull();
        assertThat(query.getTablePair()).isEqualTo(USERS_TABLE_PAIR);
        assertThat(query.getColumnPair()).isEqualTo(USERS_USERNAME_COLUMN_PAIR);
    }
}
