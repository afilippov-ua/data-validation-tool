package com.filippov.data.validation.tool.metadata;

import com.filippov.data.validation.tool.Timer;
import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.DatasourceMetadata;
import com.filippov.data.validation.tool.datasource.DatasourceTable;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToDoubleTransformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToIntegerTransformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToStringTransformer;
import com.filippov.data.validation.tool.validation.transformer.Transformer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Slf4j
public class DynamicLinkingValidationMetadataProvider implements ValidationMetadataProvider {

    @Override
    public ValidationMetadata loadMetadata(DatasourceMetadata left, DatasourceMetadata right) {
        log.debug("Loading validation metadata using dynamic linking by name provider");
        Timer timer = Timer.start();

        final List<ColumnPair> pairs = new ArrayList<>();

        final Set<String> allTables = new HashSet<>();
        allTables.addAll(left.getTables().stream().map(DatasourceTable::getName).collect(toList()));
        allTables.addAll(right.getTables().stream().map(DatasourceTable::getName).collect(toList()));

        for (String tableName : allTables) {
            final DatasourceTable leftTable = left.getTableByName(tableName);
            final DatasourceTable rightTable = left.getTableByName(tableName);

            final Set<String> allColumns = new HashSet<>();
            allColumns.addAll(leftTable.getColumns());
            allColumns.addAll(rightTable.getColumns());

            for (String columnName : allColumns) {
                final DatasourceColumn leftColumn = left.getColumnByName(tableName, columnName);
                final DatasourceColumn rightColumn = right.getColumnByName(tableName, columnName);

                if (leftColumn == null || rightColumn == null) {
                    log.error("Dynamic linking of column names error. Column with name: {} wasn't found in {} table: {}",
                            columnName, (leftColumn == null) ? "left" : "right", tableName);
                } else {
                    final Transformer defaultTransformer = getDefaultTransformer(leftColumn, rightColumn);
                    pairs.add(ColumnPair.builder()
                            .columnPairName(leftColumn.getName())
                            .left(leftColumn)
                            .right(rightColumn)
                            .leftTransformer(defaultTransformer)
                            .rightTransformer(defaultTransformer)
                            .build());
                }
            }
        }

        log.debug("Loading validation metadata using dynamic linking by name provider was finished. Execution time: {}", timer.stop());

        return ValidationMetadata.builder()
                .columnPairs(pairs)
                .build();
    }

    private Transformer<?, ?> getDefaultTransformer(DatasourceColumn left, DatasourceColumn right) {
        if (left.getDataType() == right.getDataType()) {
            switch (left.getDataType()) {
                case STRING:
                    return new ObjectToStringTransformer();
                case DOUBLE:
                    return new ObjectToDoubleTransformer();
                case INTEGER:
                    return new ObjectToIntegerTransformer();
                default:
                    throw new UnsupportedOperationException("Unsupported data type: " + left.getDataType());
            }
        } else {
            return new ObjectToStringTransformer();
        }
    }
}
