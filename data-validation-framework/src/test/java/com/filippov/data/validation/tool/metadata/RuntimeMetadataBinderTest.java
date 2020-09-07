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
import com.filippov.data.validation.tool.utils.uuid.RandomUuidRuntimeGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class RuntimeMetadataBinderTest extends AbstractTest {

    static Object[][] tablePairProvider() {
        return new Object[][]{
                {USERS, USERS_TABLE_PAIR},
                {DEPARTMENTS, DEPARTMENTS_TABLE_PAIR}
        };
    }

    static Object[][] columnPairProvider() {
        return new Object[][]{
                {USERS, USERS_ID, USERS_ID_COLUMN_PAIR},
                {USERS, USERS_USERNAME, USERS_USERNAME_COLUMN_PAIR},
                {USERS, USERS_PASSWORD, USERS_PASSWORD_COLUMN_PAIR},
                {DEPARTMENTS, DEPARTMENTS_ID, DEPARTMENTS_ID_COLUMN_PAIR},
                {DEPARTMENTS, DEPARTMENTS_NAME, DEPARTMENTS_NAME_COLUMN_PAIR},
                {DEPARTMENTS, DEPARTMENTS_NUMBER_OF_EMPLOYEES, DEPARTMENTS_NUMBER_OF_EMPLOYEES_COLUMN_PAIR}
        };
    }

    @ParameterizedTest()
    @MethodSource("tablePairProvider")
    void tablePairBinderTest(String tablePairName, TablePair expectedTablePair) {
        final Metadata bindedMetadata = new RuntimeMetadataBinder(new RandomUuidRuntimeGenerator())
                .bind(LEFT_DATASOURCE.getMetadata(), RIGHT_DATASOURCE.getMetadata());

        assertThat(bindedMetadata.getTablePairByName(tablePairName)).isNotEmpty();
        final TablePair tablePair = bindedMetadata.getTablePairByName(tablePairName).get();
        assertThat(tablePair.getName()).isEqualTo(expectedTablePair.getName());
        assertThat(tablePair.getLeftDatasourceTable()).isEqualTo(expectedTablePair.getLeftDatasourceTable());
        assertThat(tablePair.getRightDatasourceTable()).isEqualTo(expectedTablePair.getRightDatasourceTable());

        final ColumnPair keyColumnPair = tablePair.getKeyColumnPair();
        assertThat(keyColumnPair.getName()).isEqualTo(expectedTablePair.getKeyColumnPair().getName());
        assertThat(keyColumnPair.getTablePair()).isEqualTo(tablePair);
        assertThat(keyColumnPair.getLeftDatasourceColumn()).isEqualTo(expectedTablePair.getKeyColumnPair().getLeftDatasourceColumn());
        assertThat(keyColumnPair.getRightDatasourceColumn()).isEqualTo(expectedTablePair.getKeyColumnPair().getRightDatasourceColumn());
        assertThat(keyColumnPair.getLeftTransformer())
                .isExactlyInstanceOf(expectedTablePair.getKeyColumnPair().getLeftTransformer().getClass());
        assertThat(keyColumnPair.getRightTransformer())
                .isExactlyInstanceOf(expectedTablePair.getKeyColumnPair().getRightTransformer().getClass());

    }

    @ParameterizedTest()
    @MethodSource("columnPairProvider")
    void tablePairBinderTest(String tablePairName, String columnPairName, ColumnPair expectedColumnPair) {
        final Metadata bindedMetadata = new RuntimeMetadataBinder(new RandomUuidRuntimeGenerator())
                .bind(LEFT_DATASOURCE.getMetadata(), RIGHT_DATASOURCE.getMetadata());

        final Optional<TablePair> tablePairOptional = bindedMetadata.getTablePairByName(tablePairName);
        assertThat(tablePairOptional).isNotEmpty();

        final TablePair tablePair = tablePairOptional.get();

        assertThat(bindedMetadata.getColumnPairByName(tablePair, columnPairName)).isNotEmpty();

        final ColumnPair columnPair = bindedMetadata.getColumnPairByName(tablePair, columnPairName).get();
        assertThat(columnPair.getName()).isEqualTo(expectedColumnPair.getName());
        assertThat(columnPair.getTablePair()).isEqualTo(tablePair);
        assertThat(columnPair.getLeftDatasourceColumn()).isEqualTo(expectedColumnPair.getLeftDatasourceColumn());
        assertThat(columnPair.getRightDatasourceColumn()).isEqualTo(expectedColumnPair.getRightDatasourceColumn());
        assertThat(columnPair.getLeftTransformer())
                .isExactlyInstanceOf(expectedColumnPair.getLeftTransformer().getClass());
        assertThat(columnPair.getRightTransformer())
                .isExactlyInstanceOf(expectedColumnPair.getRightTransformer().getClass());
    }
}
