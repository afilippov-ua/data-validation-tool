package com.filippov.data.validation.tool.datasource;

import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.model.ColumnData;

import java.util.HashMap;
import java.util.Map;

import static com.filippov.data.validation.tool.model.DataType.DOUBLE;
import static com.filippov.data.validation.tool.model.DataType.INTEGER;
import static com.filippov.data.validation.tool.model.DataType.STRING;
import static java.util.Arrays.asList;

public class TestJsonDatasource implements Datasource {
    private static final DatasourceColumn TABLE_A_PK = DatasourceColumn.builder().name("id").dataType(INTEGER).build();
    private static final DatasourceColumn TABLE_A_INTEGER_COL = DatasourceColumn.builder().name("integerColumn").dataType(INTEGER).build();
    private static final DatasourceColumn TABLE_A_DOUBLE_COL = DatasourceColumn.builder().name("doubleColumn").dataType(DOUBLE).build();
    private static final DatasourceColumn TABLE_A_STRING_COL = DatasourceColumn.builder().name("stringColumn").dataType(STRING).build();

    private static final DatasourceColumn TABLE_B_PK = DatasourceColumn.builder().name("id").dataType(INTEGER).build();
    private static final DatasourceColumn TABLE_B_INTEGER_COL = DatasourceColumn.builder().name("integerColumn").dataType(INTEGER).build();
    private static final DatasourceColumn TABLE_B_DOUBLE_COL = DatasourceColumn.builder().name("doubleColumn").dataType(DOUBLE).build();
    private static final DatasourceColumn TABLE_B_STRING_COL = DatasourceColumn.builder().name("stringColumn").dataType(STRING).build();

    private final String fileName;
    private final Map<DatasourceColumn, ColumnData> dataMap;

    public TestJsonDatasource(String fileName) {
        this.fileName = fileName;
        this.dataMap = new HashMap<>();
    }

    @Override
    public DatasourceMetadata getMetadata() {
        return DatasourceMetadata.builder()
                .tables(asList(
                        DatasourceTable.builder()
                                .name("TableA")
                                .primaryKey(TABLE_A_PK)
                                .columns(asList(TABLE_A_PK, TABLE_A_INTEGER_COL, TABLE_A_DOUBLE_COL, TABLE_A_STRING_COL))
                                .build(),
                        DatasourceTable.builder()
                                .name("TableB")
                                .primaryKey(TABLE_B_PK)
                                .columns(asList(TABLE_B_PK, TABLE_B_INTEGER_COL, TABLE_B_DOUBLE_COL, TABLE_B_STRING_COL))
                                .build()))
                .build();
    }

    @Override
    public ColumnData getColumnData(DatasourceQuery query) {
        if (dataMap.isEmpty()) {
            loadData();
        }
        return dataMap.get(query.getColumn());
    }

    private void loadData() {

    }
}
