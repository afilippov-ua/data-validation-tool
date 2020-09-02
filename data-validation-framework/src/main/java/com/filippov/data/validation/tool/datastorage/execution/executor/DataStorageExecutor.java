package com.filippov.data.validation.tool.datastorage.execution.executor;

import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.datastorage.execution.ComparableFutureTask;
import com.filippov.data.validation.tool.datastorage.execution.Priority;

import java.util.concurrent.TimeUnit;

public class DataStorageExecutor extends PriorityThreadPoolExecutor {

    public DataStorageExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit);
    }

    public boolean containsQuery(DatasourceQuery datasourceQuery) {
        return getQueue().stream()
                .filter(runnable -> (runnable instanceof ComparableFutureTask))
                .anyMatch(runnable -> ((ComparableFutureTask<?>) runnable).getDatasourceQuery().equals(datasourceQuery));
    }

    public void remove(Priority priority, DatasourceQuery datasourceQuery) {
        getQueue().removeIf(runnable ->
                (runnable instanceof ComparableFutureTask)
                        && (((ComparableFutureTask<?>) runnable).getPriority() == priority
                        && (((ComparableFutureTask<?>) runnable).getDatasourceQuery().equals(datasourceQuery))));
    }
}
