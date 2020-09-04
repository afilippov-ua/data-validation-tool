package com.filippov.data.validation.tool.pair;

import com.filippov.data.validation.tool.model.ColumnDataInfo;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ColumnDataInfoPair {
    private final TablePair tablePair;
    private final ColumnPair columnPair;
    private final ColumnDataInfo leftColumnDataInfo;
    private final ColumnDataInfo rightColumnDataInfo;
}
