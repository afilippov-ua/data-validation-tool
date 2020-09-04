package com.filippov.data.validation.tool.dto.cache;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.filippov.data.validation.tool.model.CacheFetchingCommand;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@Builder
@ToString
public class CacheRequestDto {
    private final CacheFetchingCommand cacheFetchingCommand;

    @JsonCreator
    public CacheRequestDto(CacheFetchingCommand cacheFetchingCommand) {
        this.cacheFetchingCommand = cacheFetchingCommand;
    }
}
