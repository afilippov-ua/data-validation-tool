package com.filippov.data.validation.tool.datasource;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonDeserialize(builder = DatasourceMetadata.DatasourceMetadataBuilder.class)
public class DatasourceMetadata {
    private List<DatasourceTable> tables;

    public DatasourceTable getTableByName(String tableName) {
        return tables.stream().filter(table -> table.getName().equals(tableName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Table with name: " + tableName + " wasn't found"));
    }
}
