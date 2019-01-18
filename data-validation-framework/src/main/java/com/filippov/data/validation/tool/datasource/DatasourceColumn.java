package com.filippov.data.validation.tool.datasource;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.filippov.data.validation.tool.model.DataType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@EqualsAndHashCode(of = {"tableName", "name"})
@JsonDeserialize(builder = DatasourceColumn.DatasourceColumnBuilder.class)
public class DatasourceColumn implements Serializable {
    private String tableName;
    private String name;
    private DataType dataType;

    DatasourceColumn(String tableName, String name, DataType dataType) {
        this.tableName = tableName;
        this.name = name;
        this.dataType = dataType;
    }

    public static DatasourceColumnBuilder builder() {
        return new DatasourceColumnBuilder();
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

        public String toString() {
            return "DatasourceColumn.DatasourceColumnBuilder(tableName=" + this.tableName + ", name=" + this.name + ", dataType=" + this.dataType + ")";
        }
    }
}
