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

package com.filippov.data.validation.tool.datasource.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"name"})
public class DatasourceTable implements Serializable {
    private final String name;
    private final String primaryKey;
    private final List<String> columns;

    @JsonCreator
    DatasourceTable(@JsonProperty("name") String name, @JsonProperty("primaryKey") String primaryKey, @JsonProperty("columns") List<String> columns) {
        if (name == null || StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Incorrect input: name is null or empty");
        }
        if (primaryKey == null || StringUtils.isEmpty(primaryKey)) {
            throw new IllegalArgumentException("Incorrect input: primaryKey is null or empty");
        }
        if (columns == null) {
            throw new IllegalArgumentException("Incorrect input: columns is null");
        }
        this.name = name;
        this.primaryKey = primaryKey;
        this.columns = columns;
    }

    public String toString() {
        return "DatasourceTable(" + this.getName() + ")";
    }
}
