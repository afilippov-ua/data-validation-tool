package com.filippov.data.validation.tool.datastorage.execution.executor;

import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.datastorage.execution.ComparableFutureTask;
import com.filippov.data.validation.tool.datastorage.execution.Priority;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new PriorityBlockingQueue<>(1000));
    }

    public Future<?> submitWithPriority(Runnable task, Priority priority, DatasourceQuery datasourceQuery) {
        if (task == null) throw new NullPointerException();
        final RunnableFuture<String> ftask = newTaskWithPriorityFor(task, priority, datasourceQuery);
        execute(ftask);
        return ftask;
    }

    public <T> Future<T> submitWithPriority(Callable<T> task, Priority priority, DatasourceQuery datasourceQuery) {
        if (task == null)
            throw new NullPointerException();
        final RunnableFuture<T> ftask = newTaskWithPriorityFor(task, priority, datasourceQuery);
        execute(ftask);
        return ftask;
    }

    private <T> RunnableFuture<T> newTaskWithPriorityFor(Callable<T> callable, Priority priority, DatasourceQuery datasourceQuery) {
        return new ComparableFutureTask<T>(callable, priority, datasourceQuery);
    }

    private RunnableFuture<String> newTaskWithPriorityFor(Runnable runnable, Priority priority, DatasourceQuery datasourceQuery) {
        return new ComparableFutureTask<>(runnable, "result", priority, datasourceQuery);
    }
}
