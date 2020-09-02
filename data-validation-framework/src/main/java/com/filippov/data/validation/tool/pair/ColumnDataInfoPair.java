package com.filippov.data.validation.tool.pair;

import com.filippov.data.validation.tool.model.ColumnDataInfo;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class ColumnDataInfoPair {
    private TablePair tablePair;
    private ColumnPair columnPair;
    private ColumnDataInfo leftColumnDataInfo;
    private ColumnDataInfo rightColumnDataInfo;
}
