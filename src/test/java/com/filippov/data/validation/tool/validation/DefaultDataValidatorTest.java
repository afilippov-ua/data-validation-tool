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

package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.AbstractUnitTest;
import com.filippov.data.validation.tool.model.datasource.ColumnData;
import com.filippov.data.validation.tool.model.validation.ValidationResult;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultDataValidatorTest extends AbstractUnitTest {

    @Test
    void validatorTest() {
        final ColumnData<Integer, String> leftData = ColumnData.<Integer, String>builder()
                .keyColumn(USERS_ID_COLUMN)
                .dataColumn(USERS_USERNAME_COLUMN)
                .keys(asList(1, 2, 3, 4, 5))
                .data(asList("name1", "name2", "name3", "name4", "name5"))
                .build();

        final ColumnData<Integer, String> rightData = ColumnData.<Integer, String>builder()
                .keyColumn(USERS_ID_COLUMN)
                .dataColumn(USERS_USERNAME_COLUMN)
                .keys(asList(1, 2, 3, 4, 7))
                .data(asList("another-name", "name2", "name3", "name4", "name7"))
                .build();

        final ValidationResult<Integer> validationResult = DefaultDataValidator.builder()
                .tablePair(USERS_TABLE_PAIR)
                .keyColumnPair(USERS_ID_COLUMN_PAIR)
                .dataColumnPair(USERS_USERNAME_COLUMN_PAIR)
                .build()
                .validate(leftData, rightData);

        assertThat(validationResult).isNotNull();
        assertThat(validationResult.getTablePair()).isEqualTo(USERS_TABLE_PAIR);
        assertThat(validationResult.getKeyColumnPair()).isEqualTo(USERS_ID_COLUMN_PAIR);
        assertThat(validationResult.getDataColumnPair()).isEqualTo(USERS_USERNAME_COLUMN_PAIR);
        assertThat(validationResult.getFailedKeys())
                .isNotEmpty()
                .isEqualTo(asList(1, 5, 7));
    }
}
