package org.example;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CacheImplTypesBenchmark {

    private static final int WRITE_THREAD_COUNT = 5;
    private static final int READ_THREAD_COUNT = 5;
    private static final int TTL_SECONDS = 300;
    private static final long TEST_DURATION_MINUTES = 13;

    public static void main(String[] args) throws InterruptedException {
        Consumer dynamicCountConsumer = new DynamicCountConsumer(TTL_SECONDS);
        Consumer dequeConsumer = new DequeConsumer(TTL_SECONDS);

        extracted(dynamicCountConsumer, "DynamicCountConsumer");

        System.gc();

        extracted(dequeConsumer, "DequeConsumer");
    }

    private static void extracted(Consumer consumer, String consumerImplName) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(WRITE_THREAD_COUNT + READ_THREAD_COUNT);
        ExecutorService executorService = Executors.newFixedThreadPool(WRITE_THREAD_COUNT + READ_THREAD_COUNT);

        long startTime = System.currentTimeMillis();
        long endTime = startTime + TimeUnit.MINUTES.toMillis(TEST_DURATION_MINUTES);

        AtomicInteger totalRequests = new AtomicInteger();

        for (int i = 0; i < WRITE_THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    while (System.currentTimeMillis() < endTime) {
                        Thread.sleep(100);
                        consumer.accept((int) (Math.random() * 100));
                        totalRequests.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        for (int i = 0; i < READ_THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    while (System.currentTimeMillis() < endTime) {
                        Thread.sleep(100);
                        consumer.mean();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long totalTime = System.currentTimeMillis() - startTime;

        System.out.println(consumerImplName + " -> Total time taken (ms): " + totalTime);
        System.out.println(consumerImplName + " -> Total requests made: " + totalRequests.get());
        System.out.println(consumerImplName + " -> Requests per second (RPS): " + (totalRequests.get() * 1000.0 / totalTime));

        executorService.shutdown();
    }
}
