package com.filippov.data.validation.tool.datasource.config;

import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class JsonDatasourceConfig implements DatasourceConfig {
    private final String metadataFilePath;
    private final String dataFilePath;
    private final int maxConnections;

    @Override
    public DatasourceType getDatasourceType() {
        return DatasourceType.JSON_DATASOURCE;
    }

    @Override
    public int getMaxConnections() {
        return maxConnections;
    }
}
