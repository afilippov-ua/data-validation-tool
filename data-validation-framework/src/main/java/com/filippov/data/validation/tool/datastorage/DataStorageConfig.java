package com.filippov.data.validation.tool.datastorage;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class DataStorageConfig {
    private RelationType relationType;
    private Integer maxConnections;
}
