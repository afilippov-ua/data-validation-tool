package com.filippov.data.validation.tool.model;

import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class CacheRequest {
    private final Workspace workspace;
    private final TablePair tablePair;
    private final ColumnPair columnPair;
    private final CacheFetchingCommand cacheFetchingCommand;
}
