package com.filippov.data.validation.tool.datastorage.cache;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.DatasourceTable;
import com.filippov.data.validation.tool.model.ColumnData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class DiskColumnDataCacheTest extends AbstractTest {
    private static ColumnDataCache cache = new DiskColumnDataCache(CACHE_PATH);
    private static DatasourceTable table;
    private static DatasourceColumn pk;
    private static DatasourceColumn column;
    private static List<Integer> ids;
    private static List<String> values;
    private static ColumnData testData;

    @BeforeAll
    static void init() {
        table = DatasourceTable.builder().name("table1").build();
        pk = DatasourceColumn.builder().name("pk").table(table).build();
        column = DatasourceColumn.builder().name("column").table(table).build();

        // TODO
        table.setPrimaryKey(pk);
        table.setColumns(asList(pk, column));

        ids = asList(1, 2, 3, 4, 5, 6, 7);
        values = asList("str1", "str2", "str3", "str4", "str5", "str6", "str7");
        testData = ColumnData.builder().primaryKey(pk).column(column).keys(ids).values(values).build();
    }

    @Test
    void putColumnDataTest() {
        cache.put(column, testData);

        final Optional<ColumnData> optionalData = cache.get(column);
        assertThat(optionalData.isEmpty()).isFalse();

        final ColumnData data = optionalData.get();
        assertThat(data).isNotNull();
        assertThat(data.getPrimaryKey()).isEqualTo(pk);
        assertThat(data.getColumn()).isEqualTo(column);
        assertThat(data.getKeys()).isEqualTo(ids);
        assertThat(data.getValues()).isEqualTo(values);
    }
}
