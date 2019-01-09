package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.DatasourceTable;
import com.filippov.data.validation.tool.model.ColumnDataPair;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DataStoragePairTest extends AbstractTest {

    @Test
    void getDataTest() {
        final DatasourceTable leftTable = LEFT_DATASOURCE.getMetadata().getTableByName(TABLE_A);
        final DatasourceTable rightTable = RIGHT_DATASOURCE.getMetadata().getTableByName(TABLE_A);

        final DatasourceColumn leftColumn = LEFT_DATASOURCE.getMetadata().getColumnByName(leftTable.getName(), ID);
        final DatasourceColumn rightColumn = RIGHT_DATASOURCE.getMetadata().getColumnByName(rightTable.getName(), ID);

        final ColumnDataPair<Integer, Integer, Integer> columnData = storagePair.getColumnData(
                Query.builder()
                        .tablePair(TablePair.builder().left(leftTable).right(rightTable).build())
                        .columnPair(ColumnPair.builder().left(leftColumn).right(rightColumn).build())
                        .build());

        assertThat(columnData).isNotNull();
        assertThat(columnData.getLeft()).isNotNull();
        assertThat(columnData.getRight()).isNotNull();

        assertThat(columnData.getLeft().getPrimaryKey()).isEqualTo(leftColumn);
        assertThat(columnData.getLeft().getColumn()).isEqualTo(leftColumn);

        assertThat(columnData.getRight().getPrimaryKey()).isEqualTo(rightColumn);
        assertThat(columnData.getRight().getColumn()).isEqualTo(rightColumn);
    }
}
