package com.filippov.data.validation.tool.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@EqualsAndHashCode
public class ColumnCacheDetails {
    private boolean cached;
    private Instant date;
}
