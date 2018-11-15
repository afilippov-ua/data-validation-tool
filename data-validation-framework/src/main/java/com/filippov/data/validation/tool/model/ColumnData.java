package com.filippov.data.validation.tool.model;

import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import jdk.internal.net.http.common.Pair;

public class ColumnData {
    private DatasourceColumn primaryKey;
    private DatasourceColumn column;
    private Pair<Object[], Object[]> data;
}