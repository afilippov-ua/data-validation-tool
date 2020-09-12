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

import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString(of = {"tablePair", "keyColumnPair", "dataColumnPair"})
@Builder
public class ValidationResult<K> {
    private final TablePair tablePair;
    private final ColumnPair keyColumnPair;
    private final ColumnPair dataColumnPair;
    private final List<K> failedKeys;

    public ValidationResult(TablePair tablePair, ColumnPair keyColumnPair, ColumnPair dataColumnPair, List<K> failedKeys) {
        if (tablePair == null) {
            throw new IllegalArgumentException("Incorrect input: tablePair is null");
        }
        if (keyColumnPair == null) {
            throw new IllegalArgumentException("Incorrect input: keyColumnPair is null");
        }
        if (dataColumnPair == null) {
            throw new IllegalArgumentException("Incorrect input: dataColumnPair is null");
        }
        if (failedKeys == null) {
            throw new IllegalArgumentException("Incorrect input: failedKeys is null");
        }
        this.tablePair = tablePair;
        this.keyColumnPair = keyColumnPair;
        this.dataColumnPair = dataColumnPair;
        this.failedKeys = failedKeys;
    }
}
