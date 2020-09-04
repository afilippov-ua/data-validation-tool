package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Query {
    private final TablePair tablePair;
    private final ColumnPair columnPair;


    public String toString() {
        return "Query(" + this.getTablePair() + ", " + this.getColumnPair() + ")";
    }
}
