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

package com.filippov.data.validation.tool.pair;

import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.datastorage.RelationType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"id"})
public class TablePair {
    private String id;
    private String name;
    private ColumnPair keyColumnPair;
    private DatasourceTable leftDatasourceTable;
    private DatasourceTable rightDatasourceTable;

    public DatasourceTable getDatasourceTableFor(RelationType relationType) {
        if (relationType == LEFT) {
            return leftDatasourceTable;
        } else if (relationType == RIGHT) {
            return leftDatasourceTable;
        } else {
            throw new IllegalArgumentException("Incorrect relation type: " + relationType);
        }
    }


    public String toString() {
        return "TablePair(" + this.getName() + ")";
    }
}
