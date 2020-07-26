package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;
import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
public class DefaultDataValidator implements DataValidator {
    private final DatasourceMetadata leftMetadata;
    private final DatasourceMetadata rightMetadata;

    public DefaultDataValidator(DatasourceMetadata leftMetadata, DatasourceMetadata rightMetadata) {
        this.leftMetadata = leftMetadata;
        this.rightMetadata = rightMetadata;
    }

    @Override
    public <K, LV, RV> ValidationResult validate(TablePair tablePair, ColumnPair columnPair, ColumnData<K, LV> leftData, ColumnData<K, RV> rightData) {
        final List<K> allKeys = Stream.concat(
                leftData.getKeys().stream(),
                rightData.getKeys().stream())
                .distinct()
                .sorted() // TODO : remove this extra sorting in case of performance degradation
                .collect(toList());

        final Map<K, Integer> leftIdToIndexMap = IntStream.range(0, leftData.getKeys().size())
                .boxed()
                .collect(toMap(
                        i -> leftData.getKeys().get(i),
                        Function.identity()));

        final Map<K, Integer> rightIdToIndexMap = IntStream.range(0, rightData.getKeys().size())
                .boxed()
                .collect(toMap(
                        i -> rightData.getKeys().get(i),
                        Function.identity()));

        final List<K> failedKeys = new ArrayList<>();
        for (K key : allKeys) {
            final Integer leftId = leftIdToIndexMap.get(key);
            final Integer rightId = rightIdToIndexMap.get(key);

            if (leftId == null || rightId == null) {
                failedKeys.add(key);
            } else {
                final Object leftValue = leftData.getValues().get(leftId);
                final Object rightValue = rightData.getValues().get(rightId);

                final Object leftResult = columnPair.getLeftTransformer().transform(leftValue);
                final Object rightResult = columnPair.getRightTransformer().transform(rightValue);

                if (!Objects.equals(leftResult, rightResult)) {
                    failedKeys.add(key);
                }
            }
        }

        return ValidationResult.<K>builder()
                .tablePair(tablePair)
                .columnPair(columnPair)
                .failedKeys(failedKeys)
                .build();
    }
}
