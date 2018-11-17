package com.filippov.data.validation.tool.datasource;

import lombok.Builder;

import java.util.List;

@Builder
public class DatasourceTable {
    private String name;
    private DatasourceColumn primaryKey;
    private List<DatasourceColumn> columns;
}
