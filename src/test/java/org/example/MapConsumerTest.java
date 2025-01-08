package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapConsumerTest {

    private MapConsumer mapConsumer;
    private final int CONSUMER_TTL_SECONDS = 1;

    @BeforeEach
    public void setUp() {
        mapConsumer = new MapConsumer(CONSUMER_TTL_SECONDS);
    }

    @Test
    public void testAcceptAndMean() throws InterruptedException {
        mapConsumer.accept(1);
        mapConsumer.accept(2);

        assertEquals(1.5, mapConsumer.mean(), 0.001);
    }

    @Test
    public void testElementsExpiration() throws InterruptedException {
        mapConsumer.accept(1);

        TimeUnit.SECONDS.sleep(CONSUMER_TTL_SECONDS + 1);

        assertEquals(0.0, mapConsumer.mean(), 0.001);
    }

    @Test
    public void testSingleElement() {
        mapConsumer.accept(5);

        assertEquals(5.0, mapConsumer.mean(), 0.001);
    }

    @Test
    public void testMultipleElementsWithExpiration() throws InterruptedException {
        mapConsumer.accept(1);
        mapConsumer.accept(2);
        mapConsumer.accept(3);

        assertEquals(2.0, mapConsumer.mean(), 0.001);

        TimeUnit.SECONDS.sleep(CONSUMER_TTL_SECONDS);

        mapConsumer.accept(4);
        mapConsumer.accept(5);

        assertEquals(4.5, mapConsumer.mean(), 0.001);
    }
}