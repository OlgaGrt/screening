package org.example;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MapConsumer implements Consumer, AutoCloseable {

    /**
     * As a key we use <code> System.currentTimeMillis() *  1_000_000 + System.nanoTime() </code>
     * for unique values.
     */
    private final ConcurrentSkipListMap<Long, Integer> map = new ConcurrentSkipListMap<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final int ttlSeconds;

    public MapConsumer(int ttlSeconds) {
        this.ttlSeconds = ttlSeconds;

        executorService.scheduleAtFixedRate(() -> {
            long cutoffTime = (System.currentTimeMillis() - (ttlSeconds * 1000L)) * 1_000_000;
            map.tailMap(cutoffTime, true).clear();
        }, ttlSeconds, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void accept(int number) {
        long uniqueTimestamp = System.currentTimeMillis() * 1_000_000 + System.nanoTime();
        map.put(uniqueTimestamp, number);
    }

    @Override
    public double mean() {
        long cutoffTime = (System.currentTimeMillis() - (ttlSeconds * 1000L)) * 1_000_000;
        return map.tailMap(cutoffTime, true).values()
                .stream()
                .mapToInt(i -> i)
                .average()
                .orElse(0.0);
    }

    @Override
    public void close() throws Exception {
        executorService.shutdown();
    }
}
