package thelife.engine.tile;

import org.junit.Test;
import thelife.engine.Point;
import thelife.engine.RleParser;
import thelife.engine.testsupport.TstUtils;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UniverseSmokeTest {

    private RleParser rleParser = new RleParser();

    @Test
    public void rPentaminoEvolution() throws Exception {

        System.gc();
        System.gc();

        long usedMemoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTile = System.currentTimeMillis();
        System.out.printf("\nUsed memory before: %,15d\n", usedMemoryBefore);

        Collection<Point> initialState = rleParser.parse("Pos=-1,-1 bo$2o$b2o!");

        List<Collection<Point>> generations = TstUtils.expectedGenerations();

        Universe universe = new Universe();
        universe.setState(initialState);

        assertThat(universe.getAllAliveCells()).as("initial state").hasSameSizeAs(initialState).containsAll(initialState);

        for (int gen = 0; gen < generations.size(); gen++) {
            universe.nextGeneration();
            assertThat(universe.getAllAliveCells())
                    .as("generation " + universe.getGeneration())
                    .hasSameSizeAs(generations.get(gen))
                    .containsAll(generations.get(gen));
        }

        long spentTime = System.currentTimeMillis() -startTile;

        long usedMemoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long usedByUniverse = usedMemoryAfter - usedMemoryBefore;
        System.out.printf("Used memory after:  %,15d\nUsed by universe:   %,15d Bytes, %,11d KByte, %,7d MByte\n", usedMemoryAfter, usedByUniverse, usedByUniverse/1024, usedByUniverse/1024/1024);
        System.gc();
        System.gc();
        long usedMemoryAfterAfterGC = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long usedByUniverseAfterGC = usedMemoryAfterAfterGC - usedMemoryBefore;
        System.out.printf("After GC\nUsed memory after:  %,15d\nUsed by universe:   %,15d Bytes, %,11d KByte, %,7d MByte\n", usedMemoryAfterAfterGC, usedByUniverseAfterGC, usedByUniverseAfterGC/1024, usedByUniverseAfterGC/1024/1024);
        System.out.printf("Spent time:         %12s sec\n", spentTime/1000);

    }

}
