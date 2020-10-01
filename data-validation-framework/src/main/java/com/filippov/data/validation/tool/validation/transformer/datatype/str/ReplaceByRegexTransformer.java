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

package com.filippov.data.validation.tool.validation.transformer.datatype.str;

import com.filippov.data.validation.tool.model.DataType;
import com.filippov.data.validation.tool.validation.transformer.AbstractTransformer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReplaceByRegexTransformer extends AbstractTransformer {

    @NonNull
    private final String regex;
    @NonNull
    private final String replacement;

    @Override
    public Object transform(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return ((String) value).replaceAll(regex, replacement);
        }
        throw new IllegalArgumentException("Unsupported data type: " + value.getClass().getSimpleName());
    }

    @Override
    public DataType getInputDataType() {
        return DataType.STRING;
    }

    @Override
    public DataType getOutputDataType() {
        return DataType.STRING;
    }
}
