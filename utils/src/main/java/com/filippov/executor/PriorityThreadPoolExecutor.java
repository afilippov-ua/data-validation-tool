package com.filippov.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new PriorityBlockingQueue<>(1000));
    }

    public <T> Future<T> submit(Runnable task, T result, Priority priority) {
        if (task == null)
            throw new NullPointerException();
        RunnableFuture<T> ftask = newTaskFor(task, result, priority);
        execute(ftask);
        return ftask;
    }

    public <T> Future<T> submit(Callable<T> task, Priority priority) {
        if (task == null)
            throw new NullPointerException();
        RunnableFuture<T> ftask = newTaskFor(task, priority);
        execute(ftask);
        return ftask;
    }

    private <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value, Priority priority) {
        return new ComparableFutureTask<>(runnable, value, priority);
    }

    private <T> RunnableFuture<T> newTaskFor(Callable<T> callable, Priority priority) {
        return new ComparableFutureTask<>(callable, priority);
    }

    static class ComparableFutureTask<V> extends FutureTask<V> implements Runnable, Comparable<ComparableFutureTask<V>> {
        private Priority priority;

        ComparableFutureTask(Callable<V> callable, Priority priority) {
            super(callable);
            this.priority = priority;
        }

        ComparableFutureTask(Runnable runnable, V result, Priority priority) {
            super(runnable, result);
            this.priority = priority;
        }

        Priority getPriority() {
            return priority;
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

}
