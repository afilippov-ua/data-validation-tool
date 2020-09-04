package com.filippov.data.validation.tool.datasource.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@JsonDeserialize(builder = DatasourceMetadata.DatasourceMetadataBuilder.class)
public class DatasourceMetadata {
    private final List<DatasourceTable> tables;
    private final List<DatasourceColumn> columns;

    public boolean isTableExist(String tableName) {
        return tables.stream()
                .anyMatch(table -> table.getName().equals(tableName));
    }

    public DatasourceTable getTableByName(String tableName) {
        return tables.stream().filter(table -> table.getName().equals(tableName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Table with name: " + tableName + " wasn't found!"));
    }

    public boolean isColumnExist(String tableName, String columnName) {
        return columns.stream()
                .filter(column -> column.getTableName().equals(tableName))
                .anyMatch(column -> column.getName().equals(columnName));
    }

    public DatasourceColumn getColumnByName(String tableName, String columnName) {
        return columns.stream()
                .filter(column -> column.getTableName().equals(tableName))
                .filter(column -> column.getName().equals(columnName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Column with name: " + columnName + " for table: " + tableName + " wasn't found!"));
    }
}
