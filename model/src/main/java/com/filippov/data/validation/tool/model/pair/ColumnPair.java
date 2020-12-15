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

import com.filippov.data.validation.tool.model.Transformer;
import com.filippov.data.validation.tool.model.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.model.datastorage.RelationType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
public class ColumnPair {
    private final String id;
    private final String name;
    private final TablePair tablePair;
    private final DatasourceColumn leftDatasourceColumn;
    private final DatasourceColumn rightDatasourceColumn;

    private final Transformer<Object, ?> leftTransformer; // TODO: generics
    private final Transformer<Object, ?> rightTransformer; // TODO: generics

    public ColumnPair(String id, String name, TablePair tablePair,
                      DatasourceColumn leftDatasourceColumn, DatasourceColumn rightDatasourceColumn,
                      Transformer<Object, ?> leftTransformer, Transformer<Object, ?> rightTransformer) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("Incorrect input: id is null or empty");
        }
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Incorrect input: name is null or empty");
        }
        if (tablePair == null) {
            throw new IllegalArgumentException("Incorrect input: tablePair is null");
        }
        if (leftDatasourceColumn == null) {
            throw new IllegalArgumentException("Incorrect input: leftDatasourceColumn is null");
        }
        if (rightDatasourceColumn == null) {
            throw new IllegalArgumentException("Incorrect input: rightDatasourceColumn is null");
        }
        if (leftTransformer == null) {
            throw new IllegalArgumentException("Incorrect input: leftTransformer is null");
        }
        if (rightTransformer == null) {
            throw new IllegalArgumentException("Incorrect input: rightTransformer is null");
        }
        this.id = id;
        this.name = name;
        this.tablePair = tablePair;
        this.leftDatasourceColumn = leftDatasourceColumn;
        this.rightDatasourceColumn = rightDatasourceColumn;
        this.leftTransformer = leftTransformer;
        this.rightTransformer = rightTransformer;
    }

    public DatasourceColumn getColumnFor(RelationType relationType) {
        if (relationType == RelationType.LEFT) {
            return leftDatasourceColumn;
        } else if (relationType == RelationType.RIGHT) {
            return rightDatasourceColumn;
        } else {
            throw new IllegalArgumentException("Incorrect relation type: " + relationType);
        }
    }

    public String toString() {
        return "ColumnPair(" + this.getName() + ")";
    }
}
