package com.filippov.data.validation.tool.model;

import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.datastorage.RelationType;
import lombok.Builder;
import lombok.Getter;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;

@Getter
@Builder
public class ColumnPair {
    private DatasourceColumn left;
    private DatasourceColumn right;

    public DatasourceColumn getColumnFor(RelationType relationType) {
        if (relationType == LEFT) {
            return left;
        } else if (relationType == RIGHT) {
            return right;
        } else {
            throw new IllegalArgumentException("Incorrect relation type: " + relationType);
        }
    }
}
