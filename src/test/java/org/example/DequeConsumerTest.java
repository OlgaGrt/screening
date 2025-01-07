package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DequeConsumerTest {

    private DequeConsumer dequeConsumer;
    private final int CONSUMER_TTL_SECONDS = 1;

    @BeforeEach
    public void setUp() {
        dequeConsumer = new DequeConsumer(CONSUMER_TTL_SECONDS);
    }

    @Test
    public void testAcceptAndMean() throws InterruptedException {
        dequeConsumer.accept(1);
        dequeConsumer.accept(2);

        assertEquals(1.5, dequeConsumer.mean(), 0.001);
    }

    @Test
    public void testElementsExpiration() throws InterruptedException {
        dequeConsumer.accept(1);

        TimeUnit.SECONDS.sleep(CONSUMER_TTL_SECONDS + 1);

        assertEquals(0.0, dequeConsumer.mean(), 0.001);
    }

    @Test
    public void testSingleElement() {
        dequeConsumer.accept(5);

        assertEquals(5.0, dequeConsumer.mean(), 0.001);
    }

    @Test
    public void testMultipleElementsWithExpiration() throws InterruptedException {
        dequeConsumer.accept(1);
        dequeConsumer.accept(2);
        dequeConsumer.accept(3);

        assertEquals(2.0, dequeConsumer.mean(), 0.001);

        TimeUnit.SECONDS.sleep(CONSUMER_TTL_SECONDS);

        dequeConsumer.accept(4);
        dequeConsumer.accept(5);

        assertEquals(4.5, dequeConsumer.mean(), 0.001);
    }
}