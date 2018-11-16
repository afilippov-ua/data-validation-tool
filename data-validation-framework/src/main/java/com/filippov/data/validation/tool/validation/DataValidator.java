package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.model.ColumnData;

public interface DataValidator {

    ValidationResult validate(ColumnData left, ColumnData right);
}
