package com.filippov.data.validation.tool.dto.cache;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.filippov.data.validation.tool.dto.ColumnPairDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class ColumnPairCacheDetailsDto {
    private final ColumnPairDto columnPair;
    private final ColumnCacheDetailsDto leftCacheInfo;
    private final ColumnCacheDetailsDto rightCacheInfo;

    @JsonCreator
    public ColumnPairCacheDetailsDto(ColumnPairDto columnPair, ColumnCacheDetailsDto leftCacheInfo, ColumnCacheDetailsDto rightCacheInfo) {
        this.columnPair = columnPair;
        this.leftCacheInfo = leftCacheInfo;
        this.rightCacheInfo = rightCacheInfo;
    }
}
