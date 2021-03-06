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

package com.filippov.data.validation.tool.model.datasource;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class DatasourceQuery {
    private final DatasourceTable table;
    private final DatasourceColumn keyColumn;
    private final DatasourceColumn dataColumn;

    DatasourceQuery(DatasourceTable table, DatasourceColumn keyColumn, DatasourceColumn dataColumn) {
        if (table == null) {
            throw new IllegalArgumentException("Incorrect input: table is null");
        }
        if (keyColumn == null) {
            throw new IllegalArgumentException("Incorrect input: keyColumn is null");
        }
        if (dataColumn == null) {
            throw new IllegalArgumentException("Incorrect input: dataColumn is null");
        }
        this.table = table;
        this.keyColumn = keyColumn;
        this.dataColumn = dataColumn;
    }

    public String toString() {
        return "DatasourceQuery(keyColumn=" + this.getKeyColumn() + ", dataColumn=" + this.getDataColumn() + ")";
    }
}
