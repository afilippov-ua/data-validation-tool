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

package com.filippov.data.validation.tool.dto.workspace;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.filippov.data.validation.tool.dto.ColumnPairDto;
import com.filippov.data.validation.tool.dto.TablePairDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class WorkspaceMetadataDto {
    private final Map<String, TablePairDto> tablePairs;
    private final Map<String, Map<String, ColumnPairDto>> columnPairs;

    @JsonCreator
    public WorkspaceMetadataDto(Map<String, TablePairDto> tablePairs, Map<String, Map<String, ColumnPairDto>> columnPairs) {
        this.tablePairs = tablePairs;
        this.columnPairs = columnPairs;
    }
}
