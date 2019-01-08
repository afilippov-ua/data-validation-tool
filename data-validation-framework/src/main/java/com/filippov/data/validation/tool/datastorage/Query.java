package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class Query {
    private TablePair tablePair;
    private ColumnPair columnPair;
    private Map<String, Object> queryParams;
}
