package com.filippov.data.validation.tool.dto.cache;

import com.filippov.data.validation.tool.dto.ColumnPairDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class ColumnPairCacheDetailsDto {
    private ColumnPairDto columnPair;
    private ColumnCacheDetailsDto leftCacheInfo;
    private ColumnCacheDetailsDto rightCacheInfo;
}
