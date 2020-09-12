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
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.datastorage.DataStorage;
import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToIntegerTransformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToStringTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DataStoragePairTest extends AbstractTest {

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, RIGHT_STORAGE},
                {LEFT_STORAGE, null}
        };
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(DataStorage leftStorage, DataStorage rightStorage) {
        assertThatThrownBy(() -> new DataStoragePair(leftStorage, rightStorage))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void dataStoragePairConstructorTest() {
        final DataStoragePair columnDataPair = new DataStoragePair(LEFT_STORAGE, RIGHT_STORAGE);

        assertThat(columnDataPair).isNotNull();
        assertThat(columnDataPair.getLeftDataStorage()).isNotNull().isEqualTo(LEFT_STORAGE);
        assertThat(columnDataPair.getRightDataStorage()).isNotNull().isEqualTo(RIGHT_STORAGE);
    }

    @Test
    void DataStoragePairBuilderTest() {
        final DataStoragePair columnDataPair = DataStoragePair.builder()
                .leftDataStorage(LEFT_STORAGE)
                .rightDataStorage(RIGHT_STORAGE)
                .build();

        assertThat(columnDataPair).isNotNull();
        assertThat(columnDataPair.getLeftDataStorage()).isNotNull().isEqualTo(LEFT_STORAGE);
        assertThat(columnDataPair.getRightDataStorage()).isNotNull().isEqualTo(RIGHT_STORAGE);
    }

    @Test
    void getDataTest() {
        final DatasourceTable leftTable = LEFT_DATASOURCE.getMetadata().getTableByName(DEPARTMENTS);
        final DatasourceTable rightTable = RIGHT_DATASOURCE.getMetadata().getTableByName(DEPARTMENTS);

        final DatasourceColumn leftKeyColumn = LEFT_DATASOURCE.getMetadata().getColumnByName(leftTable.getName(), DEPARTMENTS_ID);
        final DatasourceColumn rightKeyColumn = RIGHT_DATASOURCE.getMetadata().getColumnByName(rightTable.getName(), DEPARTMENTS_ID);

        final DatasourceColumn leftDataColumn = LEFT_DATASOURCE.getMetadata().getColumnByName(leftTable.getName(), DEPARTMENTS_NAME);
        final DatasourceColumn rightDataColumn = RIGHT_DATASOURCE.getMetadata().getColumnByName(rightTable.getName(), DEPARTMENTS_NAME);

        final TablePair tablePair = TablePair.builder()
                .id("table-pair-id")
                .name("departments")
                .leftDatasourceTable(leftTable)
                .rightDatasourceTable(rightTable)
                .build();

        final ColumnPair keyColumnPair = ColumnPair.builder()
                .id("key-column-pair-id")
                .name("id")
                .tablePair(tablePair)
                .leftDatasourceColumn(leftKeyColumn)
                .rightDatasourceColumn(rightKeyColumn)
                .leftTransformer(new ObjectToIntegerTransformer())
                .rightTransformer(new ObjectToIntegerTransformer())
                .build();

        tablePair.setKeyColumnPair(keyColumnPair);

        final ColumnPair dataColumnPair = ColumnPair.builder()
                .id("column-pair-id-1")
                .name("name")
                .tablePair(tablePair)
                .leftDatasourceColumn(leftDataColumn)
                .rightDatasourceColumn(rightDataColumn)
                .leftTransformer(new ObjectToStringTransformer())
                .rightTransformer(new ObjectToStringTransformer())
                .build();

        final ColumnDataPair<Integer, String, String> columnData = STORAGE_PAIR.getColumnData(
                Query.builder()
                        .tablePair(tablePair)
                        .columnPair(dataColumnPair)
                        .build());

        assertThat(columnData).isNotNull();
        assertThat(columnData.getLeftColumnData()).isNotNull();
        assertThat(columnData.getRightColumnData()).isNotNull();

        assertThat(columnData.getLeftColumnData().getKeyColumn()).isEqualTo(leftKeyColumn);
        assertThat(columnData.getLeftColumnData().getDataColumn()).isEqualTo(leftDataColumn);

        assertThat(columnData.getRightColumnData().getKeyColumn()).isEqualTo(rightKeyColumn);
        assertThat(columnData.getRightColumnData().getDataColumn()).isEqualTo(rightDataColumn);
    }
}
