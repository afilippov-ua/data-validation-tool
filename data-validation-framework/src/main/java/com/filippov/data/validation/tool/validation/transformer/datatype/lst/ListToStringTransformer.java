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

package com.filippov.data.validation.tool.validation.transformer.datatype.lst;

import com.filippov.data.validation.tool.model.DataType;
import com.filippov.data.validation.tool.validation.transformer.AbstractTransformer;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.filippov.data.validation.tool.model.DataType.LIST;
import static com.filippov.data.validation.tool.model.DataType.STRING;

@RequiredArgsConstructor
public class ListToStringTransformer extends AbstractTransformer<List<?>, String> {
    private final String delimiter;
    private final String prefix;
    private final String suffix;

    @Override
    public String transform(List<?> lst) {
        if (lst == null) {
            return null;
        }
        return lst.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(delimiter, prefix, suffix));
    }

    @Override
    public DataType getInputDataType() {
        return LIST;
    }

    @Override
    public DataType getOutputDataType() {
        return STRING;
    }
}
