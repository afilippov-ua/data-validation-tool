package com.filippov.data.validation.tool.metadata;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datasource.DatasourceMetadata;
import com.filippov.data.validation.tool.pair.ColumnPair;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class RuntimeMetadataBinderTest extends AbstractTest {

    private RuntimeMetadataBinder provider = new RuntimeMetadataBinder();

    @Test
    void correctMetadataProcessingTest() {
        final Metadata result = provider.bind(LEFT_DATASOURCE.getMetadata(), RIGHT_DATASOURCE.getMetadata());

        final Set<ColumnPair> expectedColumnPairs = new HashSet<>(asList(
                columnPair(TABLE_A, ID),
                columnPair(TABLE_A, INTEGER_COLUMN),
                columnPair(TABLE_A, DOUBLE_COLUMN),
                columnPair(TABLE_A, STRING_COLUMN),
                columnPair(TABLE_B, ID),
                columnPair(TABLE_B, INTEGER_COLUMN),
                columnPair(TABLE_B, DOUBLE_COLUMN),
                columnPair(TABLE_B, STRING_COLUMN)));

        assertThat(result)
                .isNotNull()
                .extracting(metadata -> new HashSet<>(metadata.getColumnPairs()))
                .isEqualTo(expectedColumnPairs);
    }

    @Test
    void incorrectMetadataTest() {
        final DatasourceMetadata leftMetadata = DatasourceMetadata.builder()
                .tables(List.of(LEFT_DATASOURCE.getMetadata().getTableByName(TABLE_A).get()))
                .columns(asList(
                        LEFT_DATASOURCE.getMetadata().getColumnByName(TABLE_A, ID).get(),
                        LEFT_DATASOURCE.getMetadata().getColumnByName(TABLE_A, INTEGER_COLUMN).get(),
                        LEFT_DATASOURCE.getMetadata().getColumnByName(TABLE_A, DOUBLE_COLUMN).get()))
                .build();

        final DatasourceMetadata rightMetadata = DatasourceMetadata.builder()
                .tables(asList(RIGHT_DATASOURCE.getMetadata().getTableByName(TABLE_A).get(), RIGHT_DATASOURCE.getMetadata().getTableByName(TABLE_B).get()))
                .columns(asList(
                        RIGHT_DATASOURCE.getMetadata().getColumnByName(TABLE_A, ID).get(),
                        RIGHT_DATASOURCE.getMetadata().getColumnByName(TABLE_A, INTEGER_COLUMN).get(),
                        RIGHT_DATASOURCE.getMetadata().getColumnByName(TABLE_A, STRING_COLUMN).get(),
                        RIGHT_DATASOURCE.getMetadata().getColumnByName(TABLE_B, ID).get(),
                        RIGHT_DATASOURCE.getMetadata().getColumnByName(TABLE_B, STRING_COLUMN).get()))
                .build();

        final Metadata result = provider.bind(leftMetadata, rightMetadata);

        final Set<ColumnPair> expectedColumnPairs = new HashSet<>(asList(
                columnPair(TABLE_A, ID),
                columnPair(TABLE_A, INTEGER_COLUMN)));

        assertThat(result)
                .isNotNull()
                .extracting(metadata -> new HashSet<>(metadata.getColumnPairs()))
                .isEqualTo(expectedColumnPairs);
    }

    private ColumnPair columnPair(String tableName, String columnName) {
        return ColumnPair.builder()
                .columnPairName(columnName)
                .left(LEFT_DATASOURCE.getMetadata().getColumnByName(tableName, columnName).get())
                .right(RIGHT_DATASOURCE.getMetadata().getColumnByName(tableName, columnName).get())
                .build();
    }
}
