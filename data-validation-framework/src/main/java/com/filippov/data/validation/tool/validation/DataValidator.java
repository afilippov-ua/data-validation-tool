package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;

public interface DataValidator {

    <K, LV, RV> ValidationResult<K> validate(TablePair tablePair, ColumnPair columnPair, ColumnData<K, LV> leftData, ColumnData<K, RV> rightData);
}
