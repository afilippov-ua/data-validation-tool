package com.filippov.data.validation.tool;

public class Timer {
    private long startTime;
    private long endTime;

    public static Timer start() {
        Timer timer = new Timer();
        timer.startTime = System.currentTimeMillis();
        return timer;
    }

    public long stop() {
        endTime = System.currentTimeMillis();
        return time();
    }

    public long time() {
        return endTime - startTime;
    }
}
