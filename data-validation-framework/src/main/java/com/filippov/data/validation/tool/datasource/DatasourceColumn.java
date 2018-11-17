package com.filippov.data.validation.tool.datasource;

import com.filippov.data.validation.tool.model.DataType;
import lombok.Builder;

@Builder
public class DatasourceColumn {
    private String name;
    private DataType dataType;
}
