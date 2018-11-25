package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.model.ColumnPair;
import com.filippov.data.validation.tool.model.TablePair;
import lombok.Builder;

@Builder
public class ValidationResult {
    private TablePair tablePair;
    private ColumnPair columnPair;
    private Object[] failedKeys;
}
