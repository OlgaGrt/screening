package org.example;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DequeConsumer implements Consumer {

    private final int ttlSeconds;
    private final Deque<IntWithTimestamp> deque = new ArrayDeque<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public DequeConsumer(int ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
    }

    private static class IntWithTimestamp {
        int integer;
        Instant instant;

        public IntWithTimestamp(int integer) {
            this.integer = integer;
            instant = Instant.now();
        }
    }

    @Override
    public void accept(int number) {
        lock.writeLock().lock();
        try {
            deque.addFirst(new IntWithTimestamp(number));
            deleteTtlElements();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public double mean() {
        lock.writeLock().lock();
        try {
            deleteTtlElements();
        } finally {
            lock.writeLock().unlock();
        }

        lock.readLock().lock();
        try {
            return deque.isEmpty() ? 0.0 : deque.stream()
                    .mapToInt(x -> x.integer)
                    .average()
                    .orElse(0.0);
        } finally {
            lock.readLock().unlock();
        }
    }

    private void deleteTtlElements() {
        Instant y = Instant.now().minusSeconds(ttlSeconds);
        while (!deque.isEmpty() && deque.peekLast().instant.isBefore(y)) {
            deque.removeLast();
        }
    }
}
