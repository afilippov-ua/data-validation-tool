package com.filippov.data.validation.tool.metadata;

import com.filippov.data.validation.tool.pair.ColumnPair;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
@Builder
public class Metadata {
    private List<ColumnPair> columnPairs;

    public Optional<ColumnPair> getColumnPair(String tableName, String columnName) {
        return columnPairs.stream()
                .filter(pair -> pair.getLeft().getName().equals(columnName) || pair.getRight().getName().equals(columnName))
                .filter(pair -> pair.getLeft().getTableName().equals(tableName) || pair.getRight().getTableName().equals(tableName))
                .findFirst();
    }
}
