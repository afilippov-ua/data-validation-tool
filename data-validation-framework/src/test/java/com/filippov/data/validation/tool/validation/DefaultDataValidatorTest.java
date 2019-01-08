package com.filippov.data.validation.tool.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.DatasourceMetadata;
import com.filippov.data.validation.tool.datasource.DatasourceTable;
import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.metadata.DynamicLinkingValidationMetadataProvider;
import com.filippov.data.validation.tool.metadata.ValidationMetadata;
import com.filippov.data.validation.tool.model.ColumnDataPair;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.model.DataType;
import com.filippov.data.validation.tool.pair.TablePair;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultDataValidatorTest extends AbstractTest {

    private static final DataValidator validator = new DefaultDataValidator(LEFT_DATASOURCE.getMetadata(), RIGHT_DATASOURCE.getMetadata());

    @Test
    void test() {
        final DatasourceTable leftTable = LEFT_DATASOURCE.getMetadata().getTableByName(TABLE_A);
        final DatasourceTable rightTable = RIGHT_DATASOURCE.getMetadata().getTableByName(TABLE_A);

        final DatasourceColumn leftColumn = LEFT_DATASOURCE.getMetadata().getColumnByName(leftTable.getName(), INTEGER_COLUMN);
        final DatasourceColumn rightColumn = RIGHT_DATASOURCE.getMetadata().getColumnByName(rightTable.getName(), INTEGER_COLUMN);

        final ColumnDataPair columnDataPair = storagePair.getColumnData(Query.builder()
                .tablePair(TablePair.builder().left(leftTable).right(rightTable).build())
                .columnPair(ColumnPair.builder().left(leftColumn).right(rightColumn).build())
                .build());

        final ValidationMetadata validationMetadata = new DynamicLinkingValidationMetadataProvider().loadMetadata(LEFT_DATASOURCE.getMetadata(), RIGHT_DATASOURCE.getMetadata());
        final ColumnPair columnPair = validationMetadata.getColumnPair(leftTable.getName(), leftColumn.getName());

        final ValidationResult validationResult = validator.validate(columnPair, columnDataPair.getLeft(), columnDataPair.getRight());
        assertThat(validationResult).isNotNull();
        assertThat(validationResult.getTablePair()).isEqualTo(TablePair.builder().left(leftTable).right(rightTable).build());
        assertThat(validationResult.getColumnPair()).isEqualTo(ColumnPair.builder().left(leftColumn).right(rightColumn).build());
        assertThat(validationResult.getFailedKeys()).isNotEmpty();
    }

    @Test
    @SneakyThrows
    void test2() {
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
        System.out.println("-------------------------------------");
        System.out.println(result);
        System.out.println("-------------------------------------");

        DatasourceMetadata datasourceMetadata = new ObjectMapper().readValue(result, DatasourceMetadata.class);
        assertThat(datasourceMetadata).isNotNull();
        DatasourceTable t1 = datasourceMetadata.getTableByName("table1");
        DatasourceTable t2 = datasourceMetadata.getTableByName("table2");

        assertThat(LEFT_DATASOURCE.getMetadata().getColumnByName(t1.getName(), "PK1").getTableName()).isEqualTo(t1.getName());
        assertThat(LEFT_DATASOURCE.getMetadata().getColumnByName(t1.getName(), "col1").getTableName()).isEqualTo(t1.getName());

        assertThat(RIGHT_DATASOURCE.getMetadata().getColumnByName(t2.getName(), "PK2").getTableName()).isEqualTo(t2.getName());
        assertThat(RIGHT_DATASOURCE.getMetadata().getColumnByName(t2.getName(), "col2").getTableName()).isEqualTo(t2.getName());
    }
}
