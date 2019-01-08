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
@Getter
public class DataStoragePair {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private DataStorage left;
    private DataStorage right;

    @SneakyThrows
    public ColumnDataPair getColumnData(Query query) {
        final Future<ColumnData> leftFuture = executor.submit(() -> left.getData(query));
        final Future<ColumnData> rightFuture = executor.submit(() -> right.getData(query));

        return ColumnDataPair.builder()
                .left(leftFuture.get())
                .right(rightFuture.get())
                .build();
    }
}
