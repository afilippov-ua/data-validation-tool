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

import com.filippov.data.validation.tool.model.datasource.ColumnData;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class ColumnDataPair<K, LV, RV> {
    private final ColumnData<K, LV> leftColumnData;
    private final ColumnData<K, RV> rightColumnData;

    public ColumnDataPair(ColumnData<K, LV> leftColumnData, ColumnData<K, RV> rightColumnData) {
        if (leftColumnData == null) {
            throw new IllegalArgumentException("Incorrect input: left column data is null");
        }
        if (rightColumnData == null) {
            throw new IllegalArgumentException("Incorrect input: right column data is null");
        }
        this.leftColumnData = leftColumnData;
        this.rightColumnData = rightColumnData;
    }

    public ColumnData<K, LV> getLeftColumnData() {
        return leftColumnData;
    }

    public ColumnData<K, RV> getRightColumnData() {
        return rightColumnData;
    }
}
