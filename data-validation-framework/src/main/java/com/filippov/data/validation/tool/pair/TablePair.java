package com.filippov.data.validation.tool.pair;

import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.datastorage.RelationType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"id"})
public class TablePair {
    private String id;
    private String name;
    private ColumnPair keyColumnPair;
    private DatasourceTable leftDatasourceTable;
    private DatasourceTable rightDatasourceTable;

    public DatasourceTable getDatasourceTableFor(RelationType relationType) {
        if (relationType == LEFT) {
            return leftDatasourceTable;
        } else if (relationType == RIGHT) {
            return leftDatasourceTable;
        } else {
            throw new IllegalArgumentException("Incorrect relation type: " + relationType);
        }
    }


    public String toString() {
        return "TablePair(" + this.getName() + ")";
    }
}
