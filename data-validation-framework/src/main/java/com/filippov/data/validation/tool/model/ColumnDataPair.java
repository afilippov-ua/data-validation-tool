package com.filippov.data.validation.tool.model;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class ColumnDataPair<K, LV, RV> {
    private final ColumnData<K, LV> leftColumnData;
    private final ColumnData<K, RV> rightColumnData;

    public ColumnData<K, LV> getLeftColumnData() {
        return leftColumnData;
    }

    public ColumnData<K, RV> getRightColumnData() {
        return rightColumnData;
    }
}
