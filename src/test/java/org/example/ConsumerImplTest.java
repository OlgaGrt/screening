package org.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsumerImplTest {

    static final int CONSUMER_TTL_SECONDS = 1;

    private static Stream<Arguments> createConsumers() {
        return Stream.of(
                Arguments.of(new DequeConsumer(CONSUMER_TTL_SECONDS)),
                Arguments.of(new DynamicCountConsumer(CONSUMER_TTL_SECONDS)),
                Arguments.of(new MapConsumer(CONSUMER_TTL_SECONDS))
        );
    }

    @ParameterizedTest
    @MethodSource("createConsumers")
    public void testAcceptAndMean(Consumer consumer) {
        consumer.accept(1);
        consumer.accept(2);

        assertEquals(1.5, consumer.mean());
    }

    @ParameterizedTest
    @MethodSource("createConsumers")
    public void testElementsExpiration(Consumer consumer) throws InterruptedException {
        consumer.accept(1);

        TimeUnit.SECONDS.sleep(CONSUMER_TTL_SECONDS + 1);

        assertEquals(0.0, consumer.mean());
    }

    @ParameterizedTest
    @MethodSource("createConsumers")
    public void testSingleElement(Consumer consumer) {
        consumer.accept(5);

        assertEquals(5.0, consumer.mean());
    }

    @ParameterizedTest
    @MethodSource("createConsumers")
    public void testMultipleElementsWithExpiration(Consumer consumer) throws InterruptedException {
        consumer.accept(1);
        consumer.accept(2);
        consumer.accept(3);

        assertEquals(2.0, consumer.mean());

        TimeUnit.SECONDS.sleep(CONSUMER_TTL_SECONDS);

        consumer.accept(4);
        consumer.accept(5);

        assertEquals(4.5, consumer.mean());
    }
}