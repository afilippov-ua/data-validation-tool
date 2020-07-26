package com.filippov.data.validation.tool.model;

import com.filippov.data.validation.tool.pair.ColumnPair;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class ColumnPairCacheDetails {
    private ColumnPair columnPair;
    private ColumnCacheDetails leftCacheInfo;
    private ColumnCacheDetails rightCacheInfo;
}
