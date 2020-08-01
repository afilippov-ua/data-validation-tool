package com.filippov.data.validation.tool.dto.workspace;

import com.filippov.data.validation.tool.dto.ColumnPairDto;
import com.filippov.data.validation.tool.dto.TablePairDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@EqualsAndHashCode
public class WorkspaceMetadataDto {
    private final Map<String, TablePairDto> tablePairs;
    private final Map<String, Map<String, ColumnPairDto>> columnPairs;
}
