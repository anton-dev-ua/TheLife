package thelife;

import org.openjdk.jmh.annotations.*;
import thelife.engine.LifeAlgorithm;
import thelife.engine.RleParser;
import thelife.engine.Universe;
import thelife.engine.UniverseFactory;

import java.util.concurrent.TimeUnit;


@Fork(0)
@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class PerformanceTest {

    @State(Scope.Benchmark)
    public static class RPentamino {
        private Universe universe;

        @Setup(Level.Iteration)
        public void setup() {
            universe = new UniverseFactory().createUniverse(LifeAlgorithm.TILE);
            universe.setState(new RleParser().parse("Pos=-1,-1 bo$2o$b2o!"));
        }
    }

    @State(Scope.Benchmark)
    public static class RPentaminoIncubation {
        private Universe universe;

        @Setup(Level.Iteration)
        public void setup() {
            universe = new UniverseFactory().createUniverse(LifeAlgorithm.INCUBATION);
            universe.setState(new RleParser().parse("Pos=-1,-1 bo$2o$b2o!"));
        }
    }

    @State(Scope.Benchmark)
    public static class PufferTrain {
        private Universe universe;

        @Setup(Level.Iteration)
        public void setup() {
            universe = new UniverseFactory().createUniverse(LifeAlgorithm.TILE);
            universe.setState(new RleParser().parse("Pos=0,0 3bo$4bo$o3bo$b4o4$o$b2o$2bo$2bo$bo3$3bo$4bo$o3bo$b4o!"));
        }
    }

    @State(Scope.Benchmark)
    public static class PufferTrainIncubation {
        private Universe universe;

        @Setup(Level.Iteration)
        public void setup() {
            universe = new UniverseFactory().createUniverse(LifeAlgorithm.INCUBATION);
            universe.setState(new RleParser().parse("Pos=0,0 3bo$4bo$o3bo$b4o4$o$b2o$2bo$2bo$bo3$3bo$4bo$o3bo$b4o!"));
        }
    }

    @Benchmark
    public void rPentaminoEvolution(RPentamino testUniverese) {
        testUniverese.universe.nextGeneration();
    }

    @Benchmark
    public void rPentaminoEvolutionIncubation(RPentaminoIncubation testUniverse) {
        testUniverse.universe.nextGeneration();
    }

    @Benchmark
    public void pufferTrainEvolution(PufferTrain RPentamino) {
        RPentamino.universe.nextGeneration();
    }

    @Benchmark
    public void pufferTrainEvolutionIncubation(PufferTrainIncubation testUniverse) {
        testUniverse.universe.nextGeneration();
    }

}
