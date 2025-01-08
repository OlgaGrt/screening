package org.example;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapConsumerConcurrentTest {

    @Test
    public void testConcurrentAccess() throws InterruptedException {

        int writeThreadsCount = 5;
        int readThreadsCount = 5;
        int consumerTtlSeconds = 3;
        int testDurationSeconds = 13;

        Consumer consumer = new MapConsumer(consumerTtlSeconds);

        CountDownLatch latch = new CountDownLatch(writeThreadsCount + readThreadsCount);
        ExecutorService executorService = Executors.newFixedThreadPool(writeThreadsCount + readThreadsCount);

        long startTime = System.currentTimeMillis();
        long endTime = startTime + TimeUnit.SECONDS.toMillis(testDurationSeconds);

        for (int i = 0; i < writeThreadsCount; i++) {
            executorService.submit(() -> {
                try {
                    while (System.currentTimeMillis() < endTime) {
                        consumer.accept(new Random().nextInt(0, 1000));
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        for (int i = 0; i < readThreadsCount; i++) {
            executorService.submit(() -> {
                try {
                    while (System.currentTimeMillis() < endTime) {
                        assertEquals(500, consumer.mean(), 5);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        executorService.shutdown();
    }
}
