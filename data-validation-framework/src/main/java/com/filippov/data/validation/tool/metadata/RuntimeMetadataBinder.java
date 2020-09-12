/*
 *   Copyright 2018-2020 the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.filippov.data.validation.tool.metadata;

import com.filippov.data.validation.tool.Timer;
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.utils.uuid.UuidGenerator;
import com.filippov.data.validation.tool.validation.transformer.Transformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToDoubleTransformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToIntegerTransformer;
import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToStringTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Slf4j
@RequiredArgsConstructor
public class RuntimeMetadataBinder implements MetadataBinder {

    private final UuidGenerator uuidGenerator;

    @Override
    public Metadata bind(DatasourceMetadata leftMetadata, DatasourceMetadata rightMetadata) {
        Timer timer = Timer.start();
        log.debug("Runtime metadata binder has been started");

        final List<ColumnPair> columnPairs = new ArrayList<>();
        final List<TablePair> tablePairs = new ArrayList<>();

        final Set<String> allTableNames = Stream.concat(
                leftMetadata.getTables().stream().map(DatasourceTable::getName),
                rightMetadata.getTables().stream().map(DatasourceTable::getName))
                .collect(toSet());

        for (String tableName : allTableNames) {
            final boolean leftTableExist = leftMetadata.isTableExist(tableName);
            final boolean rightTableExist = rightMetadata.isTableExist(tableName);
            if (!leftTableExist || !rightTableExist) {
                log.error("Runtime metadata binding error. Table with name: '{}' " +
                        "wasn't found in {} datasource", tableName, (!leftTableExist) ? "left" : "right");
            } else {
                final DatasourceTable leftTable = leftMetadata.getTableByName(tableName);
                final DatasourceTable rightTable = leftMetadata.getTableByName(tableName);

                final TablePair tablePair = TablePair.builder()
                        .id(uuidGenerator.generateRandomUuid())
                        .name(leftTable.getName())
                        .leftDatasourceTable(leftTable)
                        .rightDatasourceTable(rightTable).build();
                tablePairs.add(tablePair);

                final Set<String> allColumns = new HashSet<>();
                allColumns.addAll(leftTable.getColumns());
                allColumns.addAll(rightTable.getColumns());

                for (String columnName : allColumns) {
                    final boolean leftColumnExist = leftMetadata.isColumnExist(tableName, columnName);
                    final boolean rightColumnExist = rightMetadata.isColumnExist(tableName, columnName);

                    if (!leftColumnExist || !rightColumnExist) {
                        log.error("Runtime metadata binding error. Column with name: '{}' wasn't found in {} table: '{}'",
                                columnName, (!leftColumnExist) ? "left" : "right", tableName);
                    } else {
                        final DatasourceColumn leftColumn = leftMetadata.getColumnByName(tableName, columnName);
                        final DatasourceColumn rightColumn = rightMetadata.getColumnByName(tableName, columnName);

                        final ColumnPair columnPair = ColumnPair.builder()
                                .id(uuidGenerator.generateRandomUuid())
                                .name(leftColumn.getName())
                                .tablePair(tablePair)
                                .leftDatasourceColumn(leftColumn)
                                .rightDatasourceColumn(rightColumn)
                                .leftTransformer(getDefaultTransformer(leftColumn))
                                .rightTransformer(getDefaultTransformer(rightColumn))
                                .build();

                        columnPairs.add(columnPair);
                        if (leftColumn.getName().equals(leftTable.getPrimaryKey())) {
                            tablePair.setKeyColumnPair(columnPair);
                        }
                    }
                }
            }
        }

        log.debug("Runtime metadata binder has been finished. Execution time: {}", timer.stop());

        return Metadata.builder()
                .tablePairs(tablePairs)
                .columnPairs(columnPairs)
                .build();
    }

    private Transformer getDefaultTransformer(DatasourceColumn column) {
        switch (column.getDataType()) {
            case STRING:
                return new ObjectToStringTransformer();
            case DOUBLE:
                return new ObjectToDoubleTransformer();
            case INTEGER:
                return new ObjectToIntegerTransformer();
            default:
                throw new UnsupportedOperationException("Unsupported data type: '" + column.getDataType() + "'");
        }
    }
}
