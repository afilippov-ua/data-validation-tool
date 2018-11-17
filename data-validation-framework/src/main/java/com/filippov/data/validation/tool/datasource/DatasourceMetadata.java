package com.filippov.data.validation.tool.datasource;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@JsonDeserialize(builder = DatasourceMetadata.DatasourceMetadataBuilder.class)
public class DatasourceMetadata {
    private List<DatasourceTable> tables;
}
