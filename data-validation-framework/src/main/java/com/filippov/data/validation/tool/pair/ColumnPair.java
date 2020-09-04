package com.filippov.data.validation.tool.pair;

import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datastorage.RelationType;
import com.filippov.data.validation.tool.validation.transformer.Transformer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
public class ColumnPair {
    private final String id;
    private final String name;
    private final TablePair tablePair;
    private final DatasourceColumn leftDatasourceColumn;
    private final DatasourceColumn rightDatasourceColumn;

    private final Transformer leftTransformer; // TODO: generics
    private final Transformer rightTransformer; // TODO: generics

    public DatasourceColumn getColumnFor(RelationType relationType) {
        if (relationType == LEFT) {
            return leftDatasourceColumn;
        } else if (relationType == RIGHT) {
            return rightDatasourceColumn;
        } else {
            throw new IllegalArgumentException("Incorrect relation type: " + relationType);
        }
    }


    public String toString() {
        return "ColumnPair(" + this.getName() + ")";
    }
}
