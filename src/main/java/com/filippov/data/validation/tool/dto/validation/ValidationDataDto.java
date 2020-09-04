package com.filippov.data.validation.tool.dto.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.filippov.data.validation.tool.dto.ColumnPairDto;
import com.filippov.data.validation.tool.dto.TablePairDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class ValidationDataDto {
    private final TablePairDto tablePair;
    private final ColumnPairDto keyColumnPair;
    private final ColumnPairDto dataColumnPair;
    private final List<DataRowDto> failedRows;

    @JsonCreator
    public ValidationDataDto(TablePairDto tablePair, ColumnPairDto keyColumnPair, ColumnPairDto dataColumnPair, List<DataRowDto> failedRows) {
        this.tablePair = tablePair;
        this.keyColumnPair = keyColumnPair;
        this.dataColumnPair = dataColumnPair;
        this.failedRows = failedRows;
    }
}
