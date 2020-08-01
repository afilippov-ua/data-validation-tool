package com.filippov.data.validation.tool.model;

import lombok.Builder;

@Builder
public class ColumnDataPair<K, LV, RV> {
    private ColumnData<K, LV> leftColumnData;
    private ColumnData<K, RV> rightColumnData;

    public ColumnData<K, LV> getLeftColumnData() {
        return leftColumnData;
    }

    public ColumnData<K, RV> getRightColumnData() {
        return rightColumnData;
    }
}
