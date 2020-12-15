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

package com.filippov.data.validation.tool.databinder;

import com.filippov.data.validation.tool.model.DataBinder;
import com.filippov.data.validation.tool.model.binder.DataRow;
import com.filippov.data.validation.tool.model.pair.ColumnDataPair;
import com.filippov.data.validation.tool.model.pair.ColumnPair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultDataBinder implements DataBinder {

    @Override
    public DataRow bind(ColumnDataPair<Object, Object, Object> columnDataPair, ColumnPair columnPair, Object key) {
        if (columnDataPair == null) {
            throw new IllegalArgumentException("Incorrect input: column data pair is null");
        }
        if (columnPair == null) {
            throw new IllegalArgumentException("Incorrect input: column pair is null");
        }
        if (key == null) {
            throw new IllegalArgumentException("Incorrect input: key is null");
        }

        final Object left = columnDataPair.getLeftColumnData().getValueByKey(key);
        final Object right = columnDataPair.getRightColumnData().getValueByKey(key);
        return DataRow.builder()
                .key(key)
                .leftOriginalValue(left)
                .rightOriginalValue(right)
                .leftTransformedValue(columnPair.getLeftTransformer().transform(left))
                .rightTransformedValue(columnPair.getRightTransformer().transform(right))
                .build();
    }
}
