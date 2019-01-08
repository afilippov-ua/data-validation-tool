package com.filippov.data.validation.tool.datasource;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = {"name"})
@JsonDeserialize(builder = DatasourceTable.DatasourceTableBuilder.class)
public class DatasourceTable implements Serializable {
    private String name;
    private String primaryKey;
    private List<String> columns;

    DatasourceTable(String name, String primaryKey, List<String> columns) {
        this.name = name;
        this.primaryKey = primaryKey;
        this.columns = columns;
    }

    public static DatasourceTableBuilder builder() {
        return new DatasourceTableBuilder();
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

        public String toString() {
            return "DatasourceTable.DatasourceTableBuilder(name=" + this.name + ", primaryKey=" + this.primaryKey + ", columns=" + this.columns + ")";
        }
    }
}
