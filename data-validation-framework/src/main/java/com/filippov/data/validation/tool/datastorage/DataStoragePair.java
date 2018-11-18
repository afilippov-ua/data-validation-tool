package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.model.ColumnDataPair;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DataStoragePair {
    private DataStorage left;
    private DataStorage right;

    public ColumnDataPair getColumnData(Query query) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
