package com.filippov.data.validation.tool.metadata;

import com.filippov.data.validation.tool.Timer;
import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.DatasourceMetadata;
import com.filippov.data.validation.tool.datasource.DatasourceTable;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.validation.transformer.Transformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToDoubleTransformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToIntegerTransformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToStringTransformer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Slf4j
public class RuntimeMetadataBinder implements MetadataBinder {

    @Override
    public Metadata bind(DatasourceMetadata left, DatasourceMetadata right) {
        Timer timer = Timer.start();
        log.debug("Runtime metadata binder was started");

        final List<ColumnPair> pairs = new ArrayList<>();

        final Set<String> allTables = new HashSet<>();
        allTables.addAll(left.getTables().stream().map(DatasourceTable::getName).collect(toList()));
        allTables.addAll(right.getTables().stream().map(DatasourceTable::getName).collect(toList()));

        for (String tableName : allTables) {
            final Optional<DatasourceTable> leftTable = left.getTableByName(tableName);
            final Optional<DatasourceTable> rightTable = left.getTableByName(tableName);

            if (leftTable.isPresent() && rightTable.isPresent()) {
                final Set<String> allColumns = new HashSet<>();
                allColumns.addAll(leftTable.get().getColumns());
                allColumns.addAll(rightTable.get().getColumns());

                for (String columnName : allColumns) {
                    final Optional<DatasourceColumn> leftColumn = left.getColumnByName(tableName, columnName);
                    final Optional<DatasourceColumn> rightColumn = right.getColumnByName(tableName, columnName);

                    if (leftColumn.isEmpty() || rightColumn.isEmpty()) {
                        log.error("Runtime metadata binding error. Column with name: '{}' wasn't found in {} table: '{}'",
                                columnName, leftColumn.isEmpty() ? "left" : "right", tableName);
                    } else {
                        final Transformer defaultTransformer = getDefaultTransformer(leftColumn.get(), rightColumn.get());
                        pairs.add(ColumnPair.builder()
                                .columnPairName(leftColumn.get().getName())
                                .left(leftColumn.get())
                                .right(rightColumn.get())
                                .leftTransformer(defaultTransformer)
                                .rightTransformer(defaultTransformer)
                                .build());
                    }
                }
            } else {
                log.error("Runtime metadata binding error. Table with name: '{}' wasn't found in {} datasource", tableName, leftTable.isEmpty() ? "left" : "right");
            }
        }

        log.debug("Runtime metadata binder was finished. Execution time: {}", timer.stop());

        return Metadata.builder()
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
                    throw new UnsupportedOperationException("Unsupported data type: '" + left.getDataType() + "'");
            }
        } else {
            return new ObjectToStringTransformer();
        }
    }
}
