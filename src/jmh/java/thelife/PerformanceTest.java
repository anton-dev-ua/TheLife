package thelife;

import org.openjdk.jmh.annotations.*;
import thelife.engine.Space;
import thelife.engine.Universe;

import java.util.concurrent.TimeUnit;


@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@OperationsPerInvocation(PerformanceTest.iterations)
@Warmup(iterations = 3)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.MILLISECONDS)
public class PerformanceTest {

    public static final int iterations = 10000;




    @State(Scope.Thread)
    public static class TestUniverse {
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
    }

    @State(Scope.Thread)
    public static class TestIncubationUniverse {
        private thelife.engine.incubation.Universe universe;

        @Setup(Level.Invocation)
        public void setup() {
            thelife.engine.incubation.Space space;

            space = new thelife.engine.incubation.Space();
            universe = new thelife.engine.incubation.Universe(space);

            space.setLifeAt(0, 2);
            space.setLifeAt(1, 2);
            space.setLifeAt(0, 1);
            space.setLifeAt(-1, 1);
            space.setLifeAt(0, 0);
        }
    }

    @Benchmark
    public void rPentaminoEvolution(TestUniverse testUniverse) {
        for (int i = 0; i < iterations; i++) {
            testUniverse.universe.nextGeneration();
        }
    }

    @Benchmark
    public void rPentaminoEvolutionIncubation(TestIncubationUniverse testUniverse) {
        for (int i = 0; i < iterations; i++) {
            testUniverse.universe.nextGeneration();
        }
    }

}
