package com.filippov.data.validation.tool.dto.cache;

import com.filippov.data.validation.tool.model.CacheFetchingCommand;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class CacheRequestDto {
    private CacheFetchingCommand cacheFetchingCommand;
}
