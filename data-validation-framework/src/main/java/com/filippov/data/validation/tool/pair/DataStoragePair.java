package com.filippov.data.validation.tool.pair;

import com.filippov.data.validation.tool.datastorage.DataStorage;
import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.model.ColumnDataPair;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Builder
public class DataStoragePair {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    @Getter
    private final DataStorage leftDataStorage;
    @Getter
    private final DataStorage rightDataStorage;

    @SneakyThrows
    public <K, LV, RV> ColumnDataPair<K, LV, RV> getColumnData(Query query) {
        final Future<ColumnData<K, LV>> leftFuture = executor.submit(() -> leftDataStorage.getData(query));
        final Future<ColumnData<K, RV>> rightFuture = executor.submit(() -> rightDataStorage.getData(query));

        return ColumnDataPair.<K, LV, RV>builder()
                .leftColumnData(leftFuture.get())
                .rightColumnData(rightFuture.get())
                .build();
    }
}
