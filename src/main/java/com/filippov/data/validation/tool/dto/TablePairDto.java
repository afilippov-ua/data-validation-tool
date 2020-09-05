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

package com.filippov.data.validation.tool.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.filippov.data.validation.tool.dto.datasource.DatasourceTableDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class TablePairDto {
    private final String id;
    private final String name;
    private final DatasourceTableDto leftDatasourceTable;
    private final DatasourceTableDto rightDatasourceTable;

    @JsonCreator
    public TablePairDto(String id, String name, DatasourceTableDto leftDatasourceTable, DatasourceTableDto rightDatasourceTable) {
        this.id = id;
        this.name = name;
        this.leftDatasourceTable = leftDatasourceTable;
        this.rightDatasourceTable = rightDatasourceTable;
    }
}
