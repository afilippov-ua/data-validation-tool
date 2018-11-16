package com.filippov.data.validation.tool.model;

import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import org.javatuples.Pair;

public class ColumnData {
    private DatasourceColumn primaryKey;
    private DatasourceColumn column;
    private Pair<Object[], Object[]> data;
}