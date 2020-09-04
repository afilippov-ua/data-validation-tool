package com.filippov.data.validation.tool.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ColumnDataInfo {
    private final boolean cached;
    private final Instant date;
}
