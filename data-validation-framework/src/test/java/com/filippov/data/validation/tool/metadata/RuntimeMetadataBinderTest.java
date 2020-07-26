package com.filippov.data.validation.tool.metadata;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;
import com.filippov.data.validation.tool.metadata.uuid.RandomUuidRuntimeGenerator;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class RuntimeMetadataBinderTest extends AbstractTest {

    @Test
    void correctMetadataProcessingTest() {
        final Metadata bindedMetadata = new RuntimeMetadataBinder(new RandomUuidRuntimeGenerator())
                .bind(LEFT_DATASOURCE.getMetadata(), RIGHT_DATASOURCE.getMetadata());
        final TablePair tablePair = bindedMetadata.getTablePairByName(TABLE_A).get();

        final Set<ColumnPair> expectedColumnPairs = new HashSet<>(asList(
                columnPair(tablePair, ID),
                columnPair(tablePair, INTEGER_COLUMN),
                columnPair(tablePair, DOUBLE_COLUMN),
                columnPair(tablePair, STRING_COLUMN),
                columnPair(tablePair, ID),
                columnPair(tablePair, INTEGER_COLUMN),
                columnPair(tablePair, DOUBLE_COLUMN),
                columnPair(tablePair, STRING_COLUMN)));

        assertThat(bindedMetadata)
                .isNotNull()
                .extracting(metadata -> new HashSet<>(metadata.getColumnPairs(tablePair)))
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

        final Metadata bindedMetadata = new RuntimeMetadataBinder(new RandomUuidRuntimeGenerator())
                .bind(leftMetadata, rightMetadata);
        final TablePair tablePair = bindedMetadata.getTablePairByName(TABLE_A).get();

        final Set<ColumnPair> expectedColumnPairs = new HashSet<>(asList(
                columnPair(tablePair, ID),
                columnPair(tablePair, INTEGER_COLUMN)));

        assertThat(bindedMetadata)
                .isNotNull()
                .extracting(metadata -> new HashSet<>(metadata.getColumnPairs(tablePair)))
                .isEqualTo(expectedColumnPairs);
    }

    private ColumnPair columnPair(TablePair tablePair, String columnName) {
        return ColumnPair.builder()
                .id(columnName)
                .name(columnName)
                .tablePair(tablePair)
                .left(LEFT_DATASOURCE.getMetadata().getColumnByName(tablePair.getLeft().getName(), columnName).get())
                .right(RIGHT_DATASOURCE.getMetadata().getColumnByName(tablePair.getRight().getName(), columnName).get())
                .build();
    }
}
