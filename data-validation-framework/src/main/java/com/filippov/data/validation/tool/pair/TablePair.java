package com.filippov.data.validation.tool.pair;

import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.datastorage.RelationType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;

@Getter
@Builder
@EqualsAndHashCode(of = {"left", "right"})
public class TablePair {
    private String id;
    private String name;
    private DatasourceTable left;
    private DatasourceTable right;

    public DatasourceTable getTableFor(RelationType relationType) {
        if (relationType == LEFT) {
            return left;
        } else if (relationType == RIGHT) {
            return right;
        } else {
            throw new IllegalArgumentException("Incorrect relation type: " + relationType);
        }
    }
}
