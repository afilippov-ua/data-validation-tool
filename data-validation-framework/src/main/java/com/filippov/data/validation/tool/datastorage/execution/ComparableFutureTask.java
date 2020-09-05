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

package com.filippov.data.validation.tool.datastorage.execution;

import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ComparableFutureTask<V> extends FutureTask<V> implements Runnable, Comparable<ComparableFutureTask<V>> {
    private final Priority priority;
    private final DatasourceQuery datasourceQuery;

    public ComparableFutureTask(Callable<V> callable, Priority priority, DatasourceQuery datasourceQuery) {
        super(callable);
        this.priority = priority;
        this.datasourceQuery = datasourceQuery;
    }

    public ComparableFutureTask(Runnable runnable, V result, Priority priority, DatasourceQuery datasourceQuery) {
        super(runnable, result);
        this.priority = priority;
        this.datasourceQuery = datasourceQuery;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public DatasourceQuery getDatasourceQuery() {
        return this.datasourceQuery;
    }

    @Override
    public int compareTo(ComparableFutureTask<V> o) {
        if (this.priority == null && o == null)
            return 0;
        else if (this.priority == null)
            return -1;
        else if (o == null)
            return 1;
        else {
            return this.priority.getIntValue() - o.priority.getIntValue();
        }
    }

}
