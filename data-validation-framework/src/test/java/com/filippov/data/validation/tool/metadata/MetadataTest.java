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
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToDoubleTransformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToIntegerTransformer;

import static java.util.Arrays.asList;

public class MetadataTest extends AbstractTest {

    private static final DatasourceTable LEFT_TABLE_1 = LEFT_DATASOURCE.getMetadata().getTableByName(TABLE_A);
    private static final DatasourceTable RIGHT_TABLE_1 = RIGHT_DATASOURCE.getMetadata().getTableByName(TABLE_A);

    private static final DatasourceTable LEFT_TABLE_2 = LEFT_DATASOURCE.getMetadata().getTableByName(TABLE_B);
    private static final DatasourceTable RIGHT_TABLE_2 = RIGHT_DATASOURCE.getMetadata().getTableByName(TABLE_B);

    private static final TablePair TABLE_PAIR_1 = TablePair.builder()
            .id("table-pair-id-1")
            .name(LEFT_TABLE_1.getName())
            .leftDatasourceTable(LEFT_TABLE_1)
            .rightDatasourceTable(RIGHT_TABLE_1)
            .build();

    private static final TablePair TABLE_PAIR_2 = TablePair.builder()
            .id("table-pair-id-2")
            .name(LEFT_TABLE_2.getName())
            .leftDatasourceTable(LEFT_TABLE_2)
            .rightDatasourceTable(RIGHT_TABLE_2)
            .build();

    private static final ColumnPair COLUMN_PAIR_1 = ColumnPair.builder()
            .id("column-pair-id-1")
            .tablePair(TABLE_PAIR_1)
            .name("pair_1")
            .leftDatasourceColumn(LEFT_DATASOURCE.getMetadata().getColumnByName(TABLE_A, INTEGER_COLUMN))
            .rightDatasourceColumn(RIGHT_DATASOURCE.getMetadata().getColumnByName(TABLE_A, INTEGER_COLUMN))
            .leftTransformer(new ObjectToIntegerTransformer())
            .rightTransformer(new ObjectToIntegerTransformer())
            .build();

    private static final ColumnPair COLUMN_PAIR_2 = ColumnPair.builder()
            .id("column-pair-id-2")
            .tablePair(TABLE_PAIR_2)
            .name("pair_2")
            .leftDatasourceColumn(LEFT_DATASOURCE.getMetadata().getColumnByName(TABLE_B, DOUBLE_COLUMN))
            .rightDatasourceColumn(RIGHT_DATASOURCE.getMetadata().getColumnByName(TABLE_B, DOUBLE_COLUMN))
            .leftTransformer(new ObjectToDoubleTransformer())
            .rightTransformer(new ObjectToDoubleTransformer())
            .build();

    private static final Metadata METADATA = Metadata.builder()
            .tablePairs(asList(TABLE_PAIR_1, TABLE_PAIR_2))
            .columnPairs(asList(COLUMN_PAIR_1, COLUMN_PAIR_2))
            .build();

    static Object[][] columnNameProvider() {
        return new Object[][]{
                {TABLE_A, INTEGER_COLUMN, COLUMN_PAIR_1},
                {TABLE_B, DOUBLE_COLUMN, COLUMN_PAIR_2}
        };
    }

//    @ParameterizedTest()
//    @MethodSource("columnNameProvider")
//    void getColumnPairTest(String tableName, String columnName, ColumnPair expectedColumnPair) {
//        assertThat(METADATA.getColumnPair(tableName, columnName))
//                .isNotEmpty()
//                .hasValue(expectedColumnPair);
//    }
}
