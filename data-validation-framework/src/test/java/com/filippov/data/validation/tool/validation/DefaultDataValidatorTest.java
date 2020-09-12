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

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.pair.ColumnDataPair;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToIntegerTransformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToStringTransformer;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultDataValidatorTest extends AbstractTest {

    @Test
    void validatorTest() {
        final DatasourceTable leftTable = LEFT_DATASOURCE.getMetadata().getTableByName(USERS);
        final DatasourceTable rightTable = RIGHT_DATASOURCE.getMetadata().getTableByName(USERS);
        final TablePair tablePair = TablePair.builder()
                .id(UUID_GENERATOR.generateRandomUuid())
                .name(USERS)
                .leftDatasourceTable(leftTable)
                .rightDatasourceTable(rightTable)
                .build();

        final DatasourceColumn leftKeyColumn = LEFT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_ID);
        final DatasourceColumn rightKeyColumn = RIGHT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_ID);
        final ColumnPair keyColumnPair = ColumnPair.builder()
                .id(UUID_GENERATOR.generateRandomUuid())
                .name(USERS_ID)
                .tablePair(tablePair)
                .leftDatasourceColumn(leftKeyColumn)
                .rightDatasourceColumn(rightKeyColumn)
                .leftTransformer(new ObjectToIntegerTransformer())
                .rightTransformer(new ObjectToIntegerTransformer())
                .build();

        tablePair.setKeyColumnPair(keyColumnPair);

        final DatasourceColumn leftDataColumn = LEFT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_USERNAME);
        final DatasourceColumn rightDataColumn = RIGHT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_USERNAME);
        final ColumnPair dataColumnPair = ColumnPair.builder()
                .id(UUID_GENERATOR.generateRandomUuid())
                .name(USERS_USERNAME)
                .tablePair(tablePair)
                .leftDatasourceColumn(leftDataColumn)
                .rightDatasourceColumn(rightDataColumn)
                .leftTransformer(new ObjectToStringTransformer())
                .rightTransformer(new ObjectToStringTransformer())
                .build();

        final ColumnDataPair<Integer, String, String> columnDataPair = STORAGE_PAIR.getColumnData(
                Query.builder()
                        .tablePair(tablePair)
                        .columnPair(dataColumnPair)
                        .build());

        final ValidationResult<Integer> validationResult = DefaultDataValidator.builder()
                .tablePair(tablePair)
                .keyColumnPair(keyColumnPair)
                .dataColumnPair(dataColumnPair)
                .build()
                .validate(columnDataPair.getLeftColumnData(), columnDataPair.getRightColumnData());

        assertThat(validationResult).isNotNull();
        assertThat(validationResult.getTablePair()).isEqualTo(tablePair);
        assertThat(validationResult.getKeyColumnPair()).isEqualTo(keyColumnPair);
        assertThat(validationResult.getDataColumnPair()).isEqualTo(dataColumnPair);
        assertThat(validationResult.getFailedKeys())
                .isNotEmpty()
                .isEqualTo(asList("2", "6", "7"));
    }
}
