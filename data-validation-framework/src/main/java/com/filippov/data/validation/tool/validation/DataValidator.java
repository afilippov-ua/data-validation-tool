package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.model.ColumnData;

public interface DataValidator {

    <K, LV, RV> ValidationResult<K> validate(ColumnData<K, LV> leftData, ColumnData<K, RV> rightData);
}
