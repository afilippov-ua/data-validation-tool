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
    private Workspace workspace;
    private TablePair tablePair;
    private ColumnPair columnPair;
    private CacheFetchingCommand cacheFetchingCommand;
}
