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
    private String id; // TODO!!!
    private String name;
    private TablePair tablePair;
    private DatasourceColumn leftDatasourceColumn;
    private DatasourceColumn rightDatasourceColumn;

    private Transformer leftTransformer; // TODO: generics
    private Transformer rightTransformer; // TODO: generics

    public DatasourceColumn getColumnFor(RelationType relationType) {
        if (relationType == LEFT) {
            return leftDatasourceColumn;
        } else if (relationType == RIGHT) {
            return rightDatasourceColumn;
        } else {
            throw new IllegalArgumentException("Incorrect relation type: " + relationType);
        }
    }
}
