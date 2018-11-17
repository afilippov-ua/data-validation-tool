package com.filippov.data.validation.tool.datasource;

import lombok.Builder;

import java.util.List;

@Builder
public class DatasourceMetadata {
    private List<DatasourceTable> tables;
}
