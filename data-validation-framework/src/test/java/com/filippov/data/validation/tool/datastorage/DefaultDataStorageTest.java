package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultDataStorageTest extends AbstractTest {

    static Object[][] columnProvider() {
        return new Object[][]{
                {ID, asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)},
                {INTEGER_COLUMN, asList(0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20)},
                {DOUBLE_COLUMN, asList(0.0, 4.0, 8.0, 12.0, 16.0, 20.0, 24.0, 28.0, 32.0, 36.0, 40.0)},
                {STRING_COLUMN, asList("stringValue0", "stringValue1", "stringValue2", "stringValue3", "stringValue4", "stringValue5", "stringValue6", "stringValue7",
                        "stringValue8", "stringValue9", "stringValue10")}
        };
    }

    @Test
    void getRelationTypeTest() {
        assertThat(leftStorage.getConfig().getRelationType()).isEqualTo(LEFT);
        assertThat(rightStorage.getConfig().getRelationType()).isEqualTo(RIGHT);
    }

    @Test
    void getDatasourceTest() {
        assertThat(leftStorage.getConfig().getDatasource()).isEqualTo(LEFT_DATASOURCE);
        assertThat(rightStorage.getConfig().getDatasource()).isEqualTo(RIGHT_DATASOURCE);
    }

    @ParameterizedTest()
    @MethodSource("columnProvider")
    void getDataTest(String columnName, List<?> expectedValues) {
        DatasourceMetadata leftMetadata = leftStorage.getConfig().getDatasource().getMetadata();
        DatasourceMetadata rightMetadata = rightStorage.getConfig().getDatasource().getMetadata();

        final DatasourceTable leftTable = leftMetadata.getTableByName(TABLE_A).get();
        final DatasourceTable rightTable = rightMetadata.getTableByName(TABLE_A).get();

        final DatasourceColumn leftIdColumn = leftMetadata.getColumnByName(leftTable.getName(), ID).get();

        final DatasourceColumn leftColumn = leftMetadata.getColumnByName(leftTable.getName(), columnName).get();
        final DatasourceColumn rightColumn = rightMetadata.getColumnByName(rightTable.getName(), columnName).get();

        final ColumnData<Integer, ?> data = leftStorage.getData(
                Query.builder()
                        .tablePair(TablePair.builder().id("table-pair-id-1").left(leftTable).right(rightTable).build())
                        .columnPair(ColumnPair.builder().id("column-pair-id-1").left(leftColumn).right(rightColumn).build())
                        .build());

        assertThat(data).isNotNull();
        assertThat(data.getPrimaryKey()).isEqualTo(leftIdColumn);
        assertThat(data.getColumn()).isEqualTo(leftColumn);
        assertThat(data.getKeys().stream().limit(11).collect(toList())).isEqualTo(asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        assertThat(data.getValues().stream().limit(11).collect(toList())).isEqualTo(expectedValues);
    }
}
