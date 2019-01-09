package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.DatasourceTable;
import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.metadata.DynamicLinkingValidationMetadataProvider;
import com.filippov.data.validation.tool.metadata.ValidationMetadata;
import com.filippov.data.validation.tool.model.ColumnDataPair;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultDataValidatorTest extends AbstractTest {

    private static final DataValidator validator = new DefaultDataValidator(LEFT_DATASOURCE.getMetadata(), RIGHT_DATASOURCE.getMetadata());

    @Test
    void validatorTest() {
        final DatasourceTable leftTable = LEFT_DATASOURCE.getMetadata().getTableByName(TABLE_A);
        final DatasourceTable rightTable = RIGHT_DATASOURCE.getMetadata().getTableByName(TABLE_A);

        final DatasourceColumn leftColumn = LEFT_DATASOURCE.getMetadata().getColumnByName(leftTable.getName(), INTEGER_COLUMN);
        final DatasourceColumn rightColumn = RIGHT_DATASOURCE.getMetadata().getColumnByName(rightTable.getName(), INTEGER_COLUMN);

        final ColumnDataPair<Integer, String, String> columnDataPair = storagePair.getColumnData(Query.builder()
                .tablePair(TablePair.builder().left(leftTable).right(rightTable).build())
                .columnPair(ColumnPair.builder().left(leftColumn).right(rightColumn).build())
                .build());

        final ValidationMetadata validationMetadata = new DynamicLinkingValidationMetadataProvider().loadMetadata(LEFT_DATASOURCE.getMetadata(), RIGHT_DATASOURCE.getMetadata());
        final ColumnPair columnPair = validationMetadata.getColumnPair(leftTable.getName(), leftColumn.getName());

        final ValidationResult validationResult = validator.validate(columnPair, columnDataPair.getLeft(), columnDataPair.getRight());
        assertThat(validationResult).isNotNull();
        assertThat(validationResult.getTablePair()).isEqualTo(TablePair.builder().left(leftTable).right(rightTable).build());
        assertThat(validationResult.getColumnPair()).isEqualTo(ColumnPair.builder().left(leftColumn).right(rightColumn).build());
        assertThat(validationResult.getFailedKeys())
                .isNotEmpty()
                .isEqualTo(asList(988, 989, 990, 991, 992, 993, 994, 995, 996, 997, 998, 999));
    }
}
