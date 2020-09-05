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

package com.filippov.data.validation.tool.dto.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class DataRowDto {
    private final Object key;
    private final Object leftOriginalValue;
    private final Object rightOriginalValue;
    private final Object leftTransformedValue;
    private final Object rightTransformedValue;

    @JsonCreator
    public DataRowDto(Object key, Object leftOriginalValue, Object rightOriginalValue, Object leftTransformedValue, Object rightTransformedValue) {
        this.key = key;
        this.leftOriginalValue = leftOriginalValue;
        this.rightOriginalValue = rightOriginalValue;
        this.leftTransformedValue = leftTransformedValue;
        this.rightTransformedValue = rightTransformedValue;
    }
}
