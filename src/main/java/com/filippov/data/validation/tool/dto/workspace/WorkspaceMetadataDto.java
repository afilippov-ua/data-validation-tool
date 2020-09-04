package com.filippov.data.validation.tool.dto.workspace;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.filippov.data.validation.tool.dto.ColumnPairDto;
import com.filippov.data.validation.tool.dto.TablePairDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class WorkspaceMetadataDto {
    private final Map<String, TablePairDto> tablePairs;
    private final Map<String, Map<String, ColumnPairDto>> columnPairs;

    @JsonCreator
    public WorkspaceMetadataDto(Map<String, TablePairDto> tablePairs, Map<String, Map<String, ColumnPairDto>> columnPairs) {
        this.tablePairs = tablePairs;
        this.columnPairs = columnPairs;
    }
}
