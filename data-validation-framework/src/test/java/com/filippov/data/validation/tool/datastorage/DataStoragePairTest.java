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
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.model.ColumnDataPair;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DataStoragePairTest extends AbstractTest {

    @Test
    void getDataTest() {
        final DatasourceTable leftTable = LEFT_DATASOURCE.getMetadata().getTableByName(DEPARTMENTS);
        final DatasourceTable rightTable = RIGHT_DATASOURCE.getMetadata().getTableByName(DEPARTMENTS);

        final DatasourceColumn leftKeyColumn = LEFT_DATASOURCE.getMetadata().getColumnByName(leftTable.getName(), DEPARTMENTS_ID);
        final DatasourceColumn rightKeyColumn = RIGHT_DATASOURCE.getMetadata().getColumnByName(rightTable.getName(), DEPARTMENTS_ID);

        final DatasourceColumn leftDataColumn = LEFT_DATASOURCE.getMetadata().getColumnByName(leftTable.getName(), DEPARTMENTS_NAME);
        final DatasourceColumn rightDataColumn = RIGHT_DATASOURCE.getMetadata().getColumnByName(rightTable.getName(), DEPARTMENTS_NAME);

        final ColumnPair keyColumnPair = ColumnPair.builder()
                .id("key-column-pair-id")
                .name("id")
                .leftDatasourceColumn(leftKeyColumn)
                .rightDatasourceColumn(rightKeyColumn)
                .build();

        final TablePair tablePair = TablePair.builder()
                .id("table-pair-id")
                .name("departments")
                .keyColumnPair(keyColumnPair)
                .leftDatasourceTable(leftTable)
                .rightDatasourceTable(rightTable)
                .build();


        final ColumnPair dataColumnPair = ColumnPair.builder()
                .id("column-pair-id-1")
                .name("name")
                .tablePair(tablePair)
                .leftDatasourceColumn(leftDataColumn)
                .rightDatasourceColumn(rightDataColumn)
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
