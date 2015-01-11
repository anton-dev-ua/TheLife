package thelife.engine.incubation;

import com.carrotsearch.junitbenchmarks.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import thelife.engine.RleParser;

import java.io.IOException;
import java.math.BigInteger;


public class PerformanceTest {

    @Rule
    public TestRule benchmarkRun = new BenchmarkRule(new WriterConsumer(), new ResultAsserter());

    private Universe universe;
    private static BigInteger iterations;

    @Before
    public void setUp() {
        universe = new Universe();
        universe.setState(new RleParser().parse("Pos=-1,-1 bo$2o$b2o!"));
    }


    @BenchmarkOptions(warmupRounds = 1, benchmarkRounds = 3)
    @Test
    public void rPentamino() {
        for (int i = 0; i < 1000; i++) {
            universe.nextGeneration();
        }

        iterations = universe.getGeneration();
    }

    public static class ResultAsserter implements IResultsConsumer {

        @Override
        public void accept(Result result) throws IOException {
            long avgTimeMillis = result.benchmarkTime / result.benchmarkRounds;
            BigInteger opPerSec = iterations.multiply(BigInteger.valueOf(1000)).divide(BigInteger.valueOf(avgTimeMillis));
            System.out.printf("op/s: %s (%s generations)", opPerSec, iterations);
//            assertThat(opPerSec).as(opPerSec + " evolution iterations per sec").isGreaterThan(Big10000);
        }
    }

}
