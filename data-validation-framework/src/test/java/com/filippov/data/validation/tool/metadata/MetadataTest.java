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

package com.filippov.data.validation.tool.metadata;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class MetadataTest extends AbstractTest {

    private static final Metadata METADATA = Metadata.builder()
            .tablePairs(asList(USERS_TABLE_PAIR, DEPARTMENTS_TABLE_PAIR))
            .columnPairs(asList(USERS_ID_COLUMN_PAIR, USERS_USERNAME_COLUMN_PAIR, USERS_PASSWORD_COLUMN_PAIR,
                    DEPARTMENTS_ID_COLUMN_PAIR, DEPARTMENTS_NAME_COLUMN_PAIR, DEPARTMENTS_NUMBER_OF_EMPLOYEES_COLUMN_PAIR))
            .build();

    static Object[][] columnNameProvider() {
        return new Object[][]{
                {USERS_TABLE_PAIR, USERS_ID, USERS_ID_COLUMN_PAIR},
                {USERS_TABLE_PAIR, USERS_USERNAME, USERS_USERNAME_COLUMN_PAIR},
                {USERS_TABLE_PAIR, USERS_PASSWORD, USERS_PASSWORD_COLUMN_PAIR},
                {DEPARTMENTS_TABLE_PAIR, DEPARTMENTS_ID, DEPARTMENTS_ID_COLUMN_PAIR},
                {DEPARTMENTS_TABLE_PAIR, DEPARTMENTS_NAME, DEPARTMENTS_NAME_COLUMN_PAIR},
                {DEPARTMENTS_TABLE_PAIR, DEPARTMENTS_NUMBER_OF_EMPLOYEES, DEPARTMENTS_NUMBER_OF_EMPLOYEES_COLUMN_PAIR}
        };
    }

    @ParameterizedTest()
    @MethodSource("columnNameProvider")
    void getColumnPairTest(TablePair tablePair, String columnName, ColumnPair expectedColumnPair) {
        assertThat(METADATA.getColumnPairByName(tablePair, columnName))
                .isNotEmpty()
                .hasValue(expectedColumnPair);
    }
}
