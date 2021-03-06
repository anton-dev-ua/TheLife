package thelife;

import org.openjdk.jmh.annotations.*;
import thelife.engine.LifeAlgorithm;
import thelife.engine.RleParser;
import thelife.engine.Universe;
import thelife.engine.UniverseFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Fork(value=1, jvmArgs = "-Xmx4G")
@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class PerformanceTest {

    public static Map<String, String> cellStates = new HashMap<>();
    static {
        cellStates.put("R_PENTAMINO", "Pos=-1,-1 bo$2o$b2o!");
        cellStates.put("PUFFER_TRAIN", "Pos=0,0 3bo$4bo$o3bo$b4o4$o$b2o$2bo$2bo$bo3$3bo$4bo$o3bo$b4o!");
    }


    @State(Scope.Thread)
    public static class InitialState {
        private Universe universe;

        @Param({"R_PENTAMINO", "PUFFER_TRAIN"})
//        @Param({"PUFFER_TRAIN"})
        private String a_initialState;

        @Param({"POINT", "TILE", "TILE_V2", "CACHED_QUAD_TREE", "INCUBATION"})
//        @Param({"TILE_V2", "INCUBATION", "INCUBATION_V2"})
        private LifeAlgorithm b_algorithm;

        @Setup(Level.Iteration)
        public void setup() {
            universe = new UniverseFactory().createUniverse(b_algorithm);
            universe.setState(new RleParser().parse(cellStates.get(a_initialState)));
        }
    }

    @Benchmark
    public void evolution(InitialState testUniverse) {
        testUniverse.universe.nextGeneration();
    }


}
