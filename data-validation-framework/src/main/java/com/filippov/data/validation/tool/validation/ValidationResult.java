package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ValidationResult {
    private TablePair tablePair;
    private ColumnPair columnPair;
    private List<Object> failedKeys;
}
