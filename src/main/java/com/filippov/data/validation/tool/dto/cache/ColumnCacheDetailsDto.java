package com.filippov.data.validation.tool.dto.cache;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@EqualsAndHashCode
public class ColumnCacheDetailsDto {
    private boolean cached;
    private Instant date;
}
