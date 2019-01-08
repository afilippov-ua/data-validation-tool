package com.filippov.data.validation.tool.datasource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatasourceMetadata {
    private List<DatasourceTable> tables;
    private List<DatasourceColumn> columns;

    public DatasourceTable getTableByName(String tableName) {
        return tables.stream().filter(table -> table.getName().equals(tableName))
                .findFirst()
                .orElse(null);
    }

    public DatasourceColumn getColumnByName(String tableName, String columnName) {
        return columns.stream()
                .filter(column -> column.getTableName().equals(tableName))
                .filter(column -> column.getName().equals(columnName))
                .findFirst()
                .orElse(null);
    }
}
