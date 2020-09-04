package com.filippov.data.validation.tool.datastorage;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class DataStorageConfig {
    private final RelationType relationType;
    private final Integer maxConnections;
}
