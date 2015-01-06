package thelife;

import org.openjdk.jmh.annotations.*;
import thelife.engine.Space;
import thelife.engine.Universe;

import java.util.concurrent.TimeUnit;


@Fork(1)
@State(Scope.Thread)
public class PerformanceTest {

    public static final int iterations = 10000;

    private Universe universe;

    @Setup(Level.Invocation)
    public void setup() {
        Space space;

        space = new Space();
        universe = new Universe(space);

        space.setLifeAt(0, 2);
        space.setLifeAt(1, 2);
        space.setLifeAt(0, 1);
        space.setLifeAt(-1, 1);
        space.setLifeAt(0, 0);
    }

    @OutputTimeUnit(TimeUnit.SECONDS)
    @BenchmarkMode(Mode.Throughput)
    @OperationsPerInvocation(PerformanceTest.iterations)
    @Warmup(iterations = 1)
    @Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public void rPentaminoEvolution() {
        for (int i = 0; i < iterations; i++) {
            universe.nextGeneration();
        }
    }

}
