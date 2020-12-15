/*
 *   Copyright 2018-2020 the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.filippov.data.validation.tool.model.pair;

import com.filippov.data.validation.tool.model.DataStorage;
import com.filippov.data.validation.tool.model.datasource.ColumnData;
import com.filippov.data.validation.tool.model.datastorage.Query;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Builder
public class DataStoragePair {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public DataStoragePair(DataStorage leftDataStorage, DataStorage rightDataStorage) {
        if (leftDataStorage == null) {
            throw new IllegalArgumentException("Incorrect input: leftDataStorage is null");
        }
        if (rightDataStorage == null) {
            throw new IllegalArgumentException("Incorrect input: rightDataStorage is null");
        }
        this.leftDataStorage = leftDataStorage;
        this.rightDataStorage = rightDataStorage;
    }

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
