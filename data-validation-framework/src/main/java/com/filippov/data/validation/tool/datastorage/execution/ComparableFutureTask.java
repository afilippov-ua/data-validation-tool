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
