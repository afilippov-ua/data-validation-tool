package com.filippov.data.validation.tool.dto.cache;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class ColumnCacheDetailsDto {
    private final boolean cached;
    private final Instant date;

    @JsonCreator
    public ColumnCacheDetailsDto(boolean cached, Instant date) {
        this.cached = cached;
        this.date = date;
    }
}
