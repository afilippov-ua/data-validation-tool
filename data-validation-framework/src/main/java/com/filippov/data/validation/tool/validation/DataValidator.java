package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.pair.ColumnPair;

public interface DataValidator {

    ValidationResult validate(ColumnPair columnPair, ColumnData left, ColumnData right);
}
