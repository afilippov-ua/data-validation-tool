package com.filippov.data.validation.tool.dto.validation;

import com.filippov.data.validation.tool.dto.ColumnPairDto;
import com.filippov.data.validation.tool.dto.TablePairDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ValidationDataDto {
    private TablePairDto tablePair;
    private ColumnPairDto keyColumnPair;
    private ColumnPairDto dataColumnPair;
    private List<DataRowDto> failedRows;
}
