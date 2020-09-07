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
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = {"name"})
public class DatasourceTable implements Serializable {
    private final String name;
    private final String primaryKey;
    private final List<String> columns;

    @JsonCreator
    DatasourceTable(@JsonProperty("name") String name, @JsonProperty("primaryKey") String primaryKey, @JsonProperty("columns") List<String> columns) {
        this.name = name;
        this.primaryKey = primaryKey;
        this.columns = columns;
    }

    public static DatasourceTableBuilder builder() {
        return new DatasourceTableBuilder();
    }

    public String toString() {
        return "DatasourceTable(" + this.getName() + ")";
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class DatasourceTableBuilder {
        private String name;
        private String primaryKey;
        private List<String> columns;

        DatasourceTableBuilder() {
        }

        public DatasourceTableBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DatasourceTableBuilder primaryKey(String primaryKey) {
            this.primaryKey = primaryKey;
            return this;
        }

        public DatasourceTableBuilder columns(List<String> columns) {
            this.columns = columns;
            return this;
        }

        public DatasourceTable build() {
            return new DatasourceTable(name, primaryKey, columns);
        }
    }
}
