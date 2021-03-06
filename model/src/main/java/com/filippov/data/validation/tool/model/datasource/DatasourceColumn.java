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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
@EqualsAndHashCode(of = {"tableName", "name"})
@JsonDeserialize(builder = DatasourceColumn.DatasourceColumnBuilder.class)
public class DatasourceColumn implements Serializable {
    private final String tableName;
    private final String name;
    private final DataType dataType;

    DatasourceColumn(String tableName, String name, DataType dataType) {
        validateInput(tableName, name, dataType);
        this.tableName = tableName;
        this.name = name;
        this.dataType = dataType;
    }

    public static DatasourceColumnBuilder builder() {
        return new DatasourceColumnBuilder();
    }

    private void validateInput(String tableName, String name, DataType dataType) {
        if (tableName == null) {
            throw new IllegalArgumentException("Incorrect input: tableName is null");
        }
        if (StringUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("Incorrect input: tableName is empty");
        }
        if (name == null) {
            throw new IllegalArgumentException("Incorrect input: name is null");
        }
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Incorrect input: name is empty");
        }
        if (dataType == null) {
            throw new IllegalArgumentException("Incorrect input: dataType is null");
        }
    }

    public String toString() {
        return "DatasourceColumn(" + this.getTableName() + ":" + this.getName() + ")";
    }


    @JsonPOJOBuilder(withPrefix = "")
    public static class DatasourceColumnBuilder {
        private String tableName;
        private String name;
        private DataType dataType;

        DatasourceColumnBuilder() {
        }

        public DatasourceColumnBuilder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public DatasourceColumnBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DatasourceColumnBuilder dataType(DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        public DatasourceColumn build() {
            return new DatasourceColumn(tableName, name, dataType);
        }
    }
}