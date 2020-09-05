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
import com.filippov.data.validation.tool.dto.datasource.DatasourceDefinitionDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class WorkspaceDto {
    private final String id;
    private final String name;
    private final DatasourceDefinitionDto left;
    private final DatasourceDefinitionDto right;

    @JsonCreator
    public WorkspaceDto(String id, String name, DatasourceDefinitionDto left, DatasourceDefinitionDto right) {
        this.id = id;
        this.name = name;
        this.left = left;
        this.right = right;
    }
}
