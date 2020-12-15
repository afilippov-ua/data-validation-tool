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

package com.filippov.data.validation.tool.model.validation;

import com.filippov.data.validation.tool.AbstractUnitTest;
import com.filippov.data.validation.tool.model.pair.ColumnPair;
import com.filippov.data.validation.tool.model.pair.TablePair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class ValidationResultTest extends AbstractUnitTest {

    private static final List<Integer> IDS = asList(1, 2, 3, 5);

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, USERS_ID_COLUMN_PAIR, USERS_USERNAME_COLUMN_PAIR, IDS},
                {USERS_TABLE_PAIR, null, USERS_USERNAME_COLUMN_PAIR, IDS},
                {USERS_TABLE_PAIR, USERS_ID_COLUMN_PAIR, null, IDS},
                {USERS_TABLE_PAIR, USERS_ID_COLUMN_PAIR, USERS_USERNAME_COLUMN_PAIR, null}
        };
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(TablePair tablePair, ColumnPair keyColumnPair, ColumnPair dataColumnPair, List<Integer> failedKeys) {
        Assertions.assertThatThrownBy(() -> new ValidationResult<>(tablePair, keyColumnPair, dataColumnPair, failedKeys))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void columnDataPairConstructorTest() {
        final ValidationResult<Integer> validationResult = new ValidationResult<>(USERS_TABLE_PAIR, USERS_ID_COLUMN_PAIR, USERS_USERNAME_COLUMN_PAIR, IDS);

        assertThat(validationResult).isNotNull();
        assertThat(validationResult.getTablePair()).isNotNull().isEqualTo(USERS_TABLE_PAIR);
        assertThat(validationResult.getKeyColumnPair()).isNotNull().isEqualTo(USERS_ID_COLUMN_PAIR);
        assertThat(validationResult.getDataColumnPair()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR);
        assertThat(validationResult.getFailedKeys()).isNotNull().isEqualTo(IDS);
    }

    @Test
    void columnDataPairBuilderTest() {
        final ValidationResult<Integer> validationResult = ValidationResult.<Integer>builder()
                .tablePair(USERS_TABLE_PAIR)
                .keyColumnPair(USERS_ID_COLUMN_PAIR)
                .dataColumnPair(USERS_USERNAME_COLUMN_PAIR)
                .failedKeys(IDS)
                .build();

        assertThat(validationResult).isNotNull();
        assertThat(validationResult.getTablePair()).isNotNull().isEqualTo(USERS_TABLE_PAIR);
        assertThat(validationResult.getKeyColumnPair()).isNotNull().isEqualTo(USERS_ID_COLUMN_PAIR);
        assertThat(validationResult.getDataColumnPair()).isNotNull().isEqualTo(USERS_USERNAME_COLUMN_PAIR);
        assertThat(validationResult.getFailedKeys()).isNotNull().isEqualTo(IDS);
    }
}
