package com.filippov.data.validation.tool.dto;

import com.filippov.data.validation.tool.dto.datasource.DatasourceColumnDto;
import com.filippov.data.validation.tool.dto.validation.TransformerDto;
import com.filippov.data.validation.tool.model.DataType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
public class ColumnPairDto {
    private String id;
    private String tablePairId;
    private String name;
    private DatasourceColumnDto leftDatasourceColumn;
    private DatasourceColumnDto rightDatasourceColumn;
    private List<TransformerDto> leftTransformers;
    private List<TransformerDto> rightTransformers;
    private DataType leftTransformedDataType;
    private DataType rightTransformedDataType;
}
