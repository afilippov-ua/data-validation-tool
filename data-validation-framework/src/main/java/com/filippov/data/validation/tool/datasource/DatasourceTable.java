package com.filippov.data.validation.tool.datasource;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"name"})
@JsonDeserialize(builder = DatasourceTable.DatasourceTableBuilder.class)
public class DatasourceTable implements Serializable {
    private String name;
    private DatasourceColumn primaryKey;
    private List<DatasourceColumn> columns;

    public DatasourceColumn columnByName(String columnName) {
        return columns.stream().filter(column -> column.getName().equals(columnName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Datasource column with name: " + columnName + " wasn't found in table: " + name));
    }
}
