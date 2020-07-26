package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.datasource.Datasource;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class DataStorageConfig {
    private RelationType relationType;
    private Datasource datasource;
    private Integer maxConnections;
}
