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

import com.filippov.data.validation.tool.AbstractUnitTest;
import com.filippov.data.validation.tool.TestUuidGenerator;
import com.filippov.data.validation.tool.model.Transformer;
import com.filippov.data.validation.tool.model.metadata.Metadata;
import com.filippov.data.validation.tool.model.pair.ColumnPair;
import com.filippov.data.validation.tool.model.pair.TablePair;
import com.filippov.data.validation.tool.utils.uuid.RandomUuidRuntimeGenerator;
import com.filippov.data.validation.tool.validation.transformer.datatype.obj.ObjectToIntegerTransformer;
import com.filippov.data.validation.tool.validation.transformer.datatype.obj.ObjectToStringTransformer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class RuntimeMetadataBinderTest extends AbstractUnitTest {

    static Object[][] tablePairProvider() {
        return new Object[][]{
                {USERS, USERS_TABLE_PAIR, new ObjectToStringTransformer()},
                {DEPARTMENTS, DEPARTMENTS_TABLE_PAIR, new ObjectToStringTransformer()}
        };
    }

    static Object[][] columnPairProvider() {
        return new Object[][]{
                {USERS, USERS_ID, USERS_ID_COLUMN_PAIR, new ObjectToStringTransformer()},
                {USERS, USERS_USERNAME, USERS_USERNAME_COLUMN_PAIR, new ObjectToStringTransformer()},
                {USERS, USERS_PASSWORD, USERS_PASSWORD_COLUMN_PAIR, new ObjectToStringTransformer()},
                {DEPARTMENTS, DEPARTMENTS_ID, DEPARTMENTS_ID_COLUMN_PAIR, new ObjectToStringTransformer()},
                {DEPARTMENTS, DEPARTMENTS_NAME, DEPARTMENTS_NAME_COLUMN_PAIR, new ObjectToStringTransformer()},
                {DEPARTMENTS, DEPARTMENTS_NUMBER_OF_EMPLOYEES, DEPARTMENTS_NUMBER_OF_EMPLOYEES_COLUMN_PAIR, new ObjectToIntegerTransformer()}
        };
    }

    @ParameterizedTest()
    @MethodSource("tablePairProvider")
    void tablePairBinderTest(String tablePairName, TablePair expectedTablePair, Transformer<Object, ?> expectedTransformer) {
        final Metadata bindedMetadata = new RuntimeMetadataBinder(new TestUuidGenerator())
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
                .isExactlyInstanceOf(expectedTransformer.getClass());
        assertThat(keyColumnPair.getRightTransformer())
                .isExactlyInstanceOf(expectedTransformer.getClass());

    }

    @ParameterizedTest()
    @MethodSource("columnPairProvider")
    void tablePairBinderTest(String tablePairName, String columnPairName, ColumnPair expectedColumnPair, Transformer<Object, ?> expectedTransformer) {
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
                .isExactlyInstanceOf(expectedTransformer.getClass());
        assertThat(columnPair.getRightTransformer())
                .isExactlyInstanceOf(expectedTransformer.getClass());
    }
}
