package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.pair.ColumnPair;

public interface DataValidator {

    <K, LV, RV> ValidationResult validate(ColumnPair columnPair, ColumnData<K, LV> leftData, ColumnData<K, RV> rightData);
}
