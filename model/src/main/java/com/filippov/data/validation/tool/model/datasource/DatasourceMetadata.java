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

package com.filippov.data.validation.tool.model.datasource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DatasourceMetadata {
    private final List<DatasourceTable> tables;
    private final List<DatasourceColumn> columns;

    @JsonCreator
    public DatasourceMetadata(@JsonProperty("tables") List<DatasourceTable> tables, @JsonProperty("columns") List<DatasourceColumn> columns) {
        if (tables == null) {
            throw new IllegalArgumentException("Incorrect input: tables is null");
        }
        if (columns == null) {
            throw new IllegalArgumentException("Incorrect input: columns is null");
        }
        this.tables = tables;
        this.columns = columns;
    }

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
