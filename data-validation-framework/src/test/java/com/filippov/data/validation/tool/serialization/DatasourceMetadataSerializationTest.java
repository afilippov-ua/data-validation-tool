package com.filippov.data.validation.tool.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.DatasourceMetadata;
import com.filippov.data.validation.tool.datasource.DatasourceTable;
import com.filippov.data.validation.tool.model.DataType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class DatasourceMetadataSerializationTest extends AbstractTest {

    @Test
    @SneakyThrows
    void metadataSerializationTest() {
        final DatasourceTable table1 = DatasourceTable.builder().name("table1").build();
        final DatasourceTable table2 = DatasourceTable.builder().name("table2").build();

        final DatasourceColumn pk1 = DatasourceColumn.builder().tableName(table1.getName()).name("PK1").dataType(DataType.INTEGER).build();
        final DatasourceColumn pk2 = DatasourceColumn.builder().tableName(table2.getName()).name("PK2").dataType(DataType.INTEGER).build();

        final DatasourceColumn col1 = DatasourceColumn.builder().tableName(table1.getName()).name("col1").dataType(DataType.INTEGER).build();
        final DatasourceColumn col2 = DatasourceColumn.builder().tableName(table2.getName()).name("col2").dataType(DataType.INTEGER).build();

        table1.setPrimaryKey(pk1.getName());
        table1.setColumns(asList(pk1.getName(), col1.getName()));

        table2.setPrimaryKey(pk2.getName());
        table2.setColumns(asList(pk2.getName(), col2.getName()));

        final DatasourceMetadata metadata = DatasourceMetadata.builder()
                .tables(asList(table1, table2))
                .columns(asList(pk1, pk2, col1, col2))
                .build();

        String result = new ObjectMapper().writeValueAsString(metadata);

        final DatasourceMetadata datasourceMetadata = new ObjectMapper().readValue(result, DatasourceMetadata.class);
        assertThat(datasourceMetadata).isNotNull();
        final DatasourceTable t1 = datasourceMetadata.getTableByName("table1");
        final DatasourceTable t2 = datasourceMetadata.getTableByName("table2");

        assertThat(datasourceMetadata.getColumnByName(t1.getName(), "PK1").getTableName()).isEqualTo(t1.getName());
        assertThat(datasourceMetadata.getColumnByName(t1.getName(), "col1").getTableName()).isEqualTo(t1.getName());

        assertThat(datasourceMetadata.getColumnByName(t2.getName(), "PK2").getTableName()).isEqualTo(t2.getName());
        assertThat(datasourceMetadata.getColumnByName(t2.getName(), "col2").getTableName()).isEqualTo(t2.getName());
    }
}
