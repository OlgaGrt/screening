package jmh;

import org.example.Consumer;
import org.example.DequeConsumer;
import org.example.DynamicCountConsumer;
import org.example.MapConsumer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Benchmark)
public class ConsumerAcceptAndMeanSpeedBenchmark {

    private Consumer consumer;

    @Param({"DequeConsumer", "MapConsumer", "DynamicCountConsumer"})
    private String consumerType;

    @Setup(Level.Trial)
    public void setUp() {
        switch (consumerType) {
            case "DequeConsumer":
                consumer = new DequeConsumer(3);
                break;
            case "MapConsumer":
                consumer = new MapConsumer(3);
                break;
            case "DynamicCountConsumer":
                consumer = new DynamicCountConsumer(3);
                break;
        }
    }

    @Group("testAcceptAndMeanGroup")
    @GroupThreads(5)
    @Benchmark
    public void testConsumerAcceptMethod() throws InterruptedException {
        int counter = 0;

        while (counter < 1_000) {
            consumer.accept(counter++);
        }
    }

    @Group("testAcceptAndMeanGroup")
    @GroupThreads(1)
    @Benchmark
    public double testConsumerMeanMethod() throws InterruptedException {
        int counter = 0;
        double mean = 0;

        while (counter < 1_000) {
            counter++;
            mean = consumer.mean();
        }
        return mean;
    }

    @TearDown
    public void tearDown() {
        if (consumer instanceof AutoCloseable closable) {
            try {
                closable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(ConsumerAcceptAndMeanSpeedBenchmark.class.getSimpleName())
                .warmupIterations(2)
                .measurementIterations(5)
                .forks(1)
                .addProfiler(StackProfiler.class)
                .build();

        new Runner(options).run();
    }
}