package com.filippov.data.validation.tool.metadata;

import com.filippov.data.validation.tool.pair.ColumnPair;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ValidationMetadata {
    private List<ColumnPair> columnPairs;

    public ColumnPair getColumnPair(String tableName, String columnName) {
        return columnPairs.stream()
                .filter(pair -> pair.getLeft().getName().equals(columnName) || pair.getRight().getName().equals(columnName))
                .filter(pair -> pair.getLeft().getTableName().equals(tableName) || pair.getRight().getTableName().equals(tableName))
                .findFirst()
                .orElse(null);
    }
}
