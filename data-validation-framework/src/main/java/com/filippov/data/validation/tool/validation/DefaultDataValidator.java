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

package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.Timer;
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
        log.debug("Starting validation for left data: {} and right data: {}", leftData, rightData);
        final Timer timer = Timer.start();

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

        final ValidationResult<K> result = ValidationResult.<K>builder()
                .tablePair(tablePair)
                .keyColumnPair(keyColumnPair)
                .dataColumnPair(dataColumnPair)
                .failedKeys(failedKeys)
                .build();

        log.debug("Validation execution has been finished for left data: {} and right data: {}. Number of failed keys: {}. Execution time: {}",
                leftData, rightData, failedKeys.size(), timer.stop());
        return result;
    }
}
