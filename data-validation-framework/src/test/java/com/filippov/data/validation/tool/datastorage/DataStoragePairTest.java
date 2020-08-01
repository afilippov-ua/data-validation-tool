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
