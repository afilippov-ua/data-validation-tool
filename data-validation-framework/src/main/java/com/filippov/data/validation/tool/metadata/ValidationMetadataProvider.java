package com.filippov.data.validation.tool.metadata;

import com.filippov.data.validation.tool.datasource.DatasourceMetadata;

public interface ValidationMetadataProvider {
    ValidationMetadata loadMetadata(DatasourceMetadata left, DatasourceMetadata right);
}
