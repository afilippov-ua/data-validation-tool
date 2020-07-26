package com.filippov.data.validation.tool.dto.workspace;

import com.filippov.data.validation.tool.dto.ColumnPairDto;
import com.filippov.data.validation.tool.dto.TablePairDto;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@EqualsAndHashCode
public class WorkspaceMetadataDto {
    private final Map<String, TablePairDto> tablePairs;
    private final Map<String, Map<String, ColumnPairDto>> columnPairs;
}
