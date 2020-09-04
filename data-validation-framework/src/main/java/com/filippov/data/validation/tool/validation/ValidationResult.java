package com.filippov.data.validation.tool.validation;

import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString(of = {"tablePair", "keyColumnPair", "dataColumnPair"})
@Builder
public class ValidationResult<K> {
    private final TablePair tablePair;
    private final ColumnPair keyColumnPair;
    private final ColumnPair dataColumnPair;
    private final List<K> failedKeys;
}
