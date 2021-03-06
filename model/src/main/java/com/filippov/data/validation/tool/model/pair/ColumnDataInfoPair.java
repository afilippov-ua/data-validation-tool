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

package com.filippov.data.validation.tool.model.pair;

import com.filippov.data.validation.tool.model.cache.CacheInfo;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ColumnDataInfoPair {
    private final TablePair tablePair;
    private final ColumnPair columnPair;
    private final CacheInfo leftCacheInfo;
    private final CacheInfo rightCacheInfo;

    public ColumnDataInfoPair(TablePair tablePair, ColumnPair columnPair, CacheInfo leftCacheInfo, CacheInfo rightCacheInfo) {
        if (tablePair == null) {
            throw new IllegalArgumentException("Incorrect input: tablePair is null");
        }
        if (columnPair == null) {
            throw new IllegalArgumentException("Incorrect input: columnPair is null");
        }
        if (leftCacheInfo == null) {
            throw new IllegalArgumentException("Incorrect input: leftColumnDataInfo is null");
        }
        if (rightCacheInfo == null) {
            throw new IllegalArgumentException("Incorrect input: rightColumnDataInfo is null");
        }
        this.tablePair = tablePair;
        this.columnPair = columnPair;
        this.leftCacheInfo = leftCacheInfo;
        this.rightCacheInfo = rightCacheInfo;
    }
}
