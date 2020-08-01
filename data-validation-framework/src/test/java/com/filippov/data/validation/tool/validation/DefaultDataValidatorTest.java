package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.AbstractTest;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultDataValidatorTest extends AbstractTest {

//    @Test
//    void validatorTest() {
//        final DatasourceTable leftTable = LEFT_DATASOURCE.getMetadata().getTableByName(TABLE_A).get();
//        final DatasourceTable rightTable = RIGHT_DATASOURCE.getMetadata().getTableByName(TABLE_A).get();
//        final TablePair tablePair = TablePair.builder()
//                .id(uuidGenerator.generateRandomUuid())
//                .name(TABLE_A)
//                .left(leftTable)
//                .right(rightTable)
//                .build();
//
//        final DatasourceColumn leftColumn = LEFT_DATASOURCE.getMetadata().getColumnByName(leftTable.getName(), INTEGER_COLUMN).get();
//        final DatasourceColumn rightColumn = RIGHT_DATASOURCE.getMetadata().getColumnByName(rightTable.getName(), INTEGER_COLUMN).get();
//        final ColumnPair columnPair = ColumnPair.builder()
//                .id(uuidGenerator.generateRandomUuid())
//                .tablePair(tablePair)
//                .name(INTEGER_COLUMN)
//                .left(leftColumn)
//                .right(rightColumn)
//                .leftTransformer(new ObjectToIntegerTransformer())
//                .rightTransformer(new ObjectToIntegerTransformer())
//                .build();
//
//        final ColumnDataPair<Integer, String, String> columnDataPair = storagePair.getColumnData(
//                Query.builder()
//                        .tablePair(tablePair)
//                        .columnPair(columnPair)
//                        .build());
//
//        final ValidationResult<Integer> validationResult = DefaultDataValidator.builder()
//                .tablePair(tablePair)
//                .columnPair(columnPair)
//                .build()
//                .validate(columnDataPair.getLeft(), columnDataPair.getRight());
//
//        assertThat(validationResult).isNotNull();
//        assertThat(validationResult.getTablePair()).isEqualTo(tablePair);
//        assertThat(validationResult.getColumnPair()).isEqualTo(columnPair);
//        assertThat(validationResult.getFailedKeys())
//                .isNotEmpty()
//                .isEqualTo(asList(988, 989, 990, 991, 992, 993, 994, 995, 996, 997, 998, 999));
//    }
}
