package org.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DynamicCountConsumer implements Consumer, AutoCloseable {

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(20);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private Long totalSum = 0L;
    private Integer itemCount = 0;

    private final int ttlSeconds;

    public DynamicCountConsumer(int ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
    }

    public void accept(int number) {
        lock.writeLock().lock();
        try {
            itemCount++;
            totalSum += number;
            executorService.schedule(() -> {
                try {
                    lock.writeLock().lock();
                    itemCount--;
                    totalSum -= number;
                } finally {
                    lock.writeLock().unlock();
                }
            }, ttlSeconds, TimeUnit.SECONDS);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public double mean() {
        lock.readLock().lock();
        try {
            return itemCount > 0 ? (double) totalSum / itemCount : 0;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void close() throws Exception {
        executorService.shutdown();
    }
}
