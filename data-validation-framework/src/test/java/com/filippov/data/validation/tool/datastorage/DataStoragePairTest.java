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

//    @Test
//    void getDataTest() {
//        final DatasourceTable leftTable = LEFT_DATASOURCE.getMetadata().getTableByName(TABLE_A).get();
//        final DatasourceTable rightTable = RIGHT_DATASOURCE.getMetadata().getTableByName(TABLE_A).get();
//
//        final DatasourceColumn leftColumn = LEFT_DATASOURCE.getMetadata().getColumnByName(leftTable.getName(), ID).get();
//        final DatasourceColumn rightColumn = RIGHT_DATASOURCE.getMetadata().getColumnByName(rightTable.getName(), ID).get();
//
//        final ColumnDataPair<Integer, Integer, Integer> columnData = storagePair.getColumnData(
//                Query.builder()
//                        .tablePair(TablePair.builder().id("table-pair-id-1").left(leftTable).right(rightTable).build())
//                        .columnPair(ColumnPair.builder().id("column-pair-id-1").left(leftColumn).right(rightColumn).build())
//                        .build());
//
//        assertThat(columnData).isNotNull();
//        assertThat(columnData.getLeft()).isNotNull();
//        assertThat(columnData.getRight()).isNotNull();
//
//        assertThat(columnData.getLeft().getKeyColumn()).isEqualTo(leftColumn);
//        assertThat(columnData.getLeft().getDataColumn()).isEqualTo(leftColumn);
//
//        assertThat(columnData.getRight().getKeyColumn()).isEqualTo(rightColumn);
//        assertThat(columnData.getRight().getDataColumn()).isEqualTo(rightColumn);
//    }
}
