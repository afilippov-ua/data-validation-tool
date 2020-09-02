package com.filippov.data.validation.tool.dto.cache;

import com.filippov.data.validation.tool.model.CacheFetchingCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CacheRequestDto {
    private CacheFetchingCommand cacheFetchingCommand;


}
