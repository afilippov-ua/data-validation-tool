package com.filippov.data.validation.tool.dto.datasource;

import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;
import com.filippov.data.validation.tool.datastorage.RelationType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class DatasourceDto {
    RelationType relationType;
    DatasourceMetadata metadata;
}
