package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.model.ColumnPair;
import com.filippov.data.validation.tool.model.TablePair;
import lombok.Builder;

import java.util.List;

@Builder
public class ValidationCriteria {
    private TablePair tablePair;
    private List<ColumnPair> columnPairs;
    private List<ColumnPair> extraColumns;
    private int limit;
}
