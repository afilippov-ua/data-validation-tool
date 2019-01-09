package com.filippov.data.validation.tool.model;

import com.filippov.data.validation.tool.datastorage.RelationType;
import lombok.Builder;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;

@Builder
public class ColumnDataPair<K, LV, RV> {
    private ColumnData<K, LV> left;
    private ColumnData<K, RV> right;

    public ColumnData<K, LV> getLeft() {
        return left;
    }

    public ColumnData<K, RV> getRight() {
        return right;
    }

    public ColumnData<K, ?> getColumnDataFor(RelationType relationType) {
        if (relationType == LEFT) {
            return left;
        } else if (relationType == RIGHT) {
            return right;
        } else {
            throw new IllegalArgumentException("Incorrect relation type: " + relationType);
        }
    }
}
