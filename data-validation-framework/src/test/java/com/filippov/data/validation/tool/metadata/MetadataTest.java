package com.filippov.data.validation.tool.metadata;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToDoubleTransformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToIntegerTransformer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class MetadataTest extends AbstractTest {

    private static final ColumnPair COLUMN_PAIR_1 = ColumnPair.builder()
            .columnPairName("pair_1")
            .left(LEFT_DATASOURCE.getMetadata().getColumnByName(TABLE_A, INTEGER_COLUMN).get())
            .right(RIGHT_DATASOURCE.getMetadata().getColumnByName(TABLE_A, INTEGER_COLUMN).get())
            .leftTransformer(new ObjectToIntegerTransformer())
            .rightTransformer(new ObjectToIntegerTransformer())
            .build();

    private static final ColumnPair COLUMN_PAIR_2 = ColumnPair.builder()
            .columnPairName("pair_2")
            .left(LEFT_DATASOURCE.getMetadata().getColumnByName(TABLE_B, DOUBLE_COLUMN).get())
            .right(RIGHT_DATASOURCE.getMetadata().getColumnByName(TABLE_B, DOUBLE_COLUMN).get())
            .leftTransformer(new ObjectToDoubleTransformer())
            .rightTransformer(new ObjectToDoubleTransformer())
            .build();

    private static final Metadata METADATA = Metadata.builder()
            .columnPairs(asList(COLUMN_PAIR_1, COLUMN_PAIR_2))
            .build();

    static Object[][] columnNameProvider() {
        return new Object[][]{
                {TABLE_A, INTEGER_COLUMN, COLUMN_PAIR_1},
                {TABLE_B, DOUBLE_COLUMN, COLUMN_PAIR_2}
        };
    }

    @ParameterizedTest()
    @MethodSource("columnNameProvider")
    void getColumnPairTest(String tableName, String columnName, ColumnPair expectedColumnPair) {
        assertThat(METADATA.getColumnPair(tableName, columnName))
                .isNotEmpty()
                .hasValue(expectedColumnPair);
    }
}
