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

package com.filippov.data.validation.tool.model.datastorage;

import com.filippov.data.validation.tool.model.pair.ColumnPair;
import com.filippov.data.validation.tool.model.pair.TablePair;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Query {
    private final TablePair tablePair;
    private final ColumnPair columnPair;

    public Query(TablePair tablePair, ColumnPair columnPair) {
        if (tablePair == null) {
            throw new IllegalArgumentException("Incorrect input: table pair is null");
        }
        if (columnPair == null) {
            throw new IllegalArgumentException("Incorrect input: column pair is null");
        }
        this.tablePair = tablePair;
        this.columnPair = columnPair;
    }

    public String toString() {
        return "Query(" + this.getTablePair() + ", " + this.getColumnPair() + ")";
    }
}
