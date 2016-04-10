package thelife.engine.point;

import com.carrotsearch.junitbenchmarks.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import thelife.engine.RleParser;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class PerformanceTest {

    @Rule
    public TestRule benchmarkRun = new BenchmarkRule(new WriterConsumer(), new ResultAsserter());

    private thelife.engine.Universe universe;
    private static int iterations = 10000;

    @Before
    public void setUp() {
        universe = new Universe();
        universe.setState(new RleParser().parse("Pos=-1,-1 bo$2o$b2o!"));
    }


    @BenchmarkOptions(warmupRounds = 1, benchmarkRounds = 3)
    @Test
    public void rPentamino() {
        for (int i = 0; i < iterations; i++) {
            universe.nextGeneration();
        }
    }

    public static class ResultAsserter implements IResultsConsumer {

        @Override
        public void accept(Result result) throws IOException {
            long avgTimeMillis = result.benchmarkTime / result.benchmarkRounds;
            long opPerSec = iterations * 1000 / avgTimeMillis;
            System.out.println("op/s: " + opPerSec);
            assertThat(opPerSec).as(opPerSec + " evolution iterations per sec").isGreaterThan(10000);
        }
    }

}
