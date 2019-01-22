package com.filippov.data.validation.tool.pair;

import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.datastorage.RelationType;
import com.filippov.data.validation.tool.validation.transformer.Transformer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;

@Getter
@Builder
@EqualsAndHashCode(of = {"columnPairName", "left", "right"})
public class ColumnPair {
    private String columnPairName;
    private DatasourceColumn left;
    private DatasourceColumn right;

    private Transformer<? super Object, ? extends Object> leftTransformer; // TODO: generics
    private Transformer<? super Object, ? extends Object> rightTransformer; // TODO: generics

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