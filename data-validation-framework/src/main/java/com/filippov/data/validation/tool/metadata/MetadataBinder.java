package com.filippov.data.validation.tool.metadata;

import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;

public interface MetadataBinder {
    Metadata bind(DatasourceMetadata left, DatasourceMetadata right);
}
