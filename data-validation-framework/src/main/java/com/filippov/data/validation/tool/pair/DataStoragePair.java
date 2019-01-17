package com.filippov.data.validation.tool.pair;

import com.filippov.data.validation.tool.datastorage.DataStorage;
import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.model.ColumnDataPair;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Builder
@Getter
@EqualsAndHashCode(of = {"left", "right"})
public class DataStoragePair {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private DataStorage left;
    private DataStorage right;

    @SneakyThrows
    public <K, LV, RV> ColumnDataPair<K, LV, RV> getColumnData(Query query) {
        final Future<ColumnData<K, LV>> leftFuture = executor.submit(() -> left.getData(query));
        final Future<ColumnData<K, RV>> rightFuture = executor.submit(() -> right.getData(query));

        return ColumnDataPair.<K, LV, RV>builder()
                .left(leftFuture.get())
                .right(rightFuture.get())
                .build();
    }
}
