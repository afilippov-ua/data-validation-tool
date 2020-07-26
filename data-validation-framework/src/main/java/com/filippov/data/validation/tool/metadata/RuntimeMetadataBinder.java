package com.filippov.data.validation.tool.metadata;

import com.filippov.data.validation.tool.Timer;
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.metadata.uuid.UuidGenerator;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.validation.transformer.Transformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToDoubleTransformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToIntegerTransformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToStringTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class RuntimeMetadataBinder implements MetadataBinder {

    private final UuidGenerator uuidGenerator;

    @Override
    public Metadata bind(DatasourceMetadata left, DatasourceMetadata right) {
        Timer timer = Timer.start();
        log.debug("Runtime metadata binder was started");

        final List<ColumnPair> columnPairs = new ArrayList<>();
        final List<TablePair> tablePairs = new ArrayList<>();

        final Set<String> allTableNames = new HashSet<>();
        allTableNames.addAll(left.getTables().stream().map(DatasourceTable::getName).collect(toList()));
        allTableNames.addAll(right.getTables().stream().map(DatasourceTable::getName).collect(toList()));

        for (String tableName : allTableNames) {
            final Optional<DatasourceTable> leftTable = left.getTableByName(tableName); //TODO: slow
            final Optional<DatasourceTable> rightTable = left.getTableByName(tableName); //TODO: slow

            if (leftTable.isPresent() && rightTable.isPresent()) {
                final TablePair tablePair = TablePair.builder()
                        .id(uuidGenerator.generateRandomUuid())
                        .name(leftTable.get().getName())
                        .left(leftTable.get())
                        .right(rightTable.get()).build();
                tablePairs.add(tablePair);

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
                        columnPairs.add(ColumnPair.builder()
                                .id(uuidGenerator.generateRandomUuid())
                                .name(leftColumn.get().getName())
                                .tablePair(tablePair)
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
                .tablePairs(tablePairs)
                .columnPairs(columnPairs)
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
