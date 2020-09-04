package com.filippov.data.validation.tool.dto.datasource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;
import com.filippov.data.validation.tool.datastorage.RelationType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class DatasourceDto {
    private final RelationType relationType;
    private final DatasourceMetadata metadata;

    @JsonCreator
    public DatasourceDto(RelationType relationType, DatasourceMetadata metadata) {
        this.relationType = relationType;
        this.metadata = metadata;
    }
}
