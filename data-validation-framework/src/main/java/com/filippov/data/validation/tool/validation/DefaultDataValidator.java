package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@Builder
public class DefaultDataValidator implements DataValidator {

    private final TablePair tablePair;
    private final ColumnPair keyColumnPair;
    private final ColumnPair dataColumnPair;

    @Override
    public <K, LV, RV> ValidationResult<K> validate(ColumnData<K, LV> leftData, ColumnData<K, RV> rightData) {
        final List<K> allKeys = Stream.concat(
                leftData.getKeys().stream(),
                rightData.getKeys().stream())
                .distinct()
                .sorted() // TODO : remove this extra sorting in case of performance degradation
                .collect(toList());

        final List<K> failedKeys = new ArrayList<>();
        for (K key : allKeys) {
            if (!leftData.containsKey(key) || !rightData.containsKey(key)) {
                failedKeys.add(key);
            } else {
                final Object leftValue = leftData.getValueByKey(key);
                final Object rightValue = rightData.getValueByKey(key);

                final Object leftResult = dataColumnPair.getLeftTransformer().transform(leftValue);
                final Object rightResult = dataColumnPair.getRightTransformer().transform(rightValue);

                if (!Objects.equals(leftResult, rightResult)) {
                    failedKeys.add(key);
                }
            }
        }

        return ValidationResult.<K>builder()
                .tablePair(tablePair)
                .keyColumnPair(keyColumnPair)
                .dataColumnPair(dataColumnPair)
                .failedKeys(failedKeys)
                .build();
    }
}
