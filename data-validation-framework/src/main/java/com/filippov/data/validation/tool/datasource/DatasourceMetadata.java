package com.filippov.data.validation.tool.datasource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatasourceMetadata {
    private List<DatasourceTable> tables;
    private List<DatasourceColumn> columns;

    public Optional<DatasourceTable> getTableByName(String tableName) {
        return tables.stream().filter(table -> table.getName().equals(tableName))
                .findFirst();
    }

    public Optional<DatasourceColumn> getColumnByName(String tableName, String columnName) {
        return columns.stream()
                .filter(column -> column.getTableName().equals(tableName))
                .filter(column -> column.getName().equals(columnName))
                .findFirst();
    }
}
