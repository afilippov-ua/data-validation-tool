package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.datasource.DatasourceMetadata;
import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class DefaultDataValidator implements DataValidator {
    private final DatasourceMetadata leftMetadata;
    private final DatasourceMetadata rightMetadata;

    public DefaultDataValidator(DatasourceMetadata leftMetadata, DatasourceMetadata rightMetadata) {
        this.leftMetadata = leftMetadata;
        this.rightMetadata = rightMetadata;
    }

    @Override
    public ValidationResult validate(ColumnPair columnPair, ColumnData left, ColumnData right) {
        final List<Object> allKeys = Stream.concat(
                left.getKeys().stream(),
                right.getKeys().stream())
                .distinct()
                .sorted() // TODO : remove this extra sorting in case of performance degradation
                .collect(toList());

        final List<Object> failedKeys = new ArrayList<>();
        for (Object key : allKeys) {

        }

        return ValidationResult.builder()
                .tablePair(TablePair.builder()
                        .left(leftMetadata.getTableByName(left.getColumn().getTableName()))
                        .right(rightMetadata.getTableByName(right.getColumn().getTableName()))
                        .build())
                .columnPair(ColumnPair.builder().left(left.getColumn()).right(right.getColumn()).build())
                .failedKeys(failedKeys)
                .build();
    }
}
