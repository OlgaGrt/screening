package jmh;

import org.example.Consumer;
import org.example.DequeConsumer;
import org.example.DynamicCountConsumer;
import org.example.MapConsumer;
import org.openjdk.jmh.annotations.Benchmark;
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
public class ConsumerAcceptSpeedBenchmark {

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


    @Benchmark
    public void testConsumerAcceptMethod() throws InterruptedException {
        int counter = 0;

        while (counter < 1_000_000) {
            consumer.accept(counter++);
        }
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
                .include(ConsumerAcceptSpeedBenchmark.class.getSimpleName())
                .warmupIterations(2)
                .measurementIterations(5)
                .forks(1)
                .addProfiler(StackProfiler.class)
                .build();

        new Runner(options).run();
    }
}


/*
# Parameters: (consumerType = DequeConsumer)

Result "jmh.ConsumerAcceptSpeedBenchmark.testConsumerAcceptMethod":
  7,766 ±(99.9%) 1,064 ops/s [Average]
  (min, avg, max) = (7,423, 7,766, 8,065), stdev = 0,276
  CI (99.9%): [6,702, 8,830] (assumes normal distribution)

Secondary result "jmh.ConsumerAcceptSpeedBenchmark.testConsumerAcceptMethod:stack":
Stack profiler:

....[Thread state distributions]....................................................................
 75,0%         RUNNABLE
 25,0%         TIMED_WAITING



# Parameters: (consumerType = MapConsumer)

Result "jmh.ConsumerAcceptSpeedBenchmark.testConsumerAcceptMethod":
  3,738 ±(99.9%) 2,134 ops/s [Average]
  (min, avg, max) = (2,908, 3,738, 4,431), stdev = 0,554
  CI (99.9%): [1,604, 5,872] (assumes normal distribution)

Secondary result "jmh.ConsumerAcceptSpeedBenchmark.testConsumerAcceptMethod:stack":
Stack profiler:

....[Thread state distributions]....................................................................
 74,9%         RUNNABLE
 25,1%         TIMED_WAITING



# Parameters: (consumerType = DynamicCountConsumer)

Result "jmh.ConsumerAcceptSpeedBenchmark.testConsumerAcceptMethod":
  0,318 ±(99.9%) 0,953 ops/s [Average]
  (min, avg, max) = (0,050, 0,318, 0,594), stdev = 0,247
  CI (99.9%): [≈ 0, 1,270] (assumes normal distribution)

Secondary result "jmh.ConsumerAcceptSpeedBenchmark.testConsumerAcceptMethod:stack":
Stack profiler:

....[Thread state distributions]....................................................................
 85,0%         WAITING
 10,7%         RUNNABLE
  4,3%         TIMED_WAITING

# Run complete. Total time: 00:05:40


Benchmark                                                          (consumerType)   Mode  Cnt  Score   Error  Units
ConsumerAcceptSpeedBenchmark.testConsumerAcceptMethod               DequeConsumer  thrpt    5  7,766 ± 1,064  ops/s
ConsumerAcceptSpeedBenchmark.testConsumerAcceptMethod:stack         DequeConsumer  thrpt         NaN            ---
ConsumerAcceptSpeedBenchmark.testConsumerAcceptMethod                 MapConsumer  thrpt    5  3,738 ± 2,134  ops/s
ConsumerAcceptSpeedBenchmark.testConsumerAcceptMethod:stack           MapConsumer  thrpt         NaN            ---
ConsumerAcceptSpeedBenchmark.testConsumerAcceptMethod        DynamicCountConsumer  thrpt    5  0,318 ± 0,953  ops/s
ConsumerAcceptSpeedBenchmark.testConsumerAcceptMethod:stack  DynamicCountConsumer  thrpt         NaN            ---

Process finished with exit code 0

 */