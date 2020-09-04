package com.filippov.data.validation.tool.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.filippov.data.validation.tool.dto.datasource.DatasourceColumnDto;
import com.filippov.data.validation.tool.dto.validation.TransformerDto;
import com.filippov.data.validation.tool.model.DataType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class ColumnPairDto {
    private final String id;
    private final String tablePairId;
    private final String name;
    private final DatasourceColumnDto leftDatasourceColumn;
    private final DatasourceColumnDto rightDatasourceColumn;
    private final List<TransformerDto> leftTransformers;
    private final List<TransformerDto> rightTransformers;
    private final DataType leftTransformedDataType;
    private final DataType rightTransformedDataType;

    @JsonCreator
    public ColumnPairDto(String id, String tablePairId, String name,
                         DatasourceColumnDto leftDatasourceColumn, DatasourceColumnDto rightDatasourceColumn,
                         List<TransformerDto> leftTransformers, List<TransformerDto> rightTransformers,
                         DataType leftTransformedDataType, DataType rightTransformedDataType) {
        this.id = id;
        this.tablePairId = tablePairId;
        this.name = name;
        this.leftDatasourceColumn = leftDatasourceColumn;
        this.rightDatasourceColumn = rightDatasourceColumn;
        this.leftTransformers = leftTransformers;
        this.rightTransformers = rightTransformers;
        this.leftTransformedDataType = leftTransformedDataType;
        this.rightTransformedDataType = rightTransformedDataType;
    }
}
