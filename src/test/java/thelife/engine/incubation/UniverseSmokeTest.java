package thelife.engine.incubation;

import org.junit.Test;
import thelife.engine.Point;
import thelife.engine.RleParser;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static thelife.engine.testsupport.TstUtils.expectedGenerations;

public class UniverseSmokeTest {

    private RleParser rleParser = new RleParser();

    @Test
    public void rPentaminoEvolution() throws Exception {

        System.gc();
        System.gc();

        long usedMemoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.printf("\nUsed memory before: %,15d\n", usedMemoryBefore);

        Collection<Point> initialState = rleParser.parse("Pos=-1,-1 bo$2o$b2o!");

        List<Collection<Point>> generations = expectedGenerations();

//        thelife.engine.Universe universe = new Universe();
        thelife.engine.Universe universe = new CachedQuadTreeUniverse();
        universe.setState(initialState);

//        Thread.sleep(5000);

        assertThat(universe.getAllAliveCells()).as("initial state").hasSameSizeAs(initialState).containsAll(initialState);

        long startTile = System.currentTimeMillis();
        for (int i = 0; i < 50000; i++) {
            universe.nextGeneration();
//            if(i%1000 == 0) System.out.println("done "+i);
//            System.out.printf("%s, ",universe.getGeneration());

//            int gen = (int)universe.getGeneration().longValue();
//            if(gen == 1000) {
////                System.out.println(gen + ", " + new RleFormatter().format(universe.getAllAliveCells()));
//                if (gen < generations.size()) {
//                    Collection<Point> allAliveCells = universe.getAllAliveCells();
//                    assertThat(allAliveCells)
//                            .as("generation " + gen)
//                            .hasSameSizeAs(generations.get(gen - 1))
//                            .containsAll(generations.get(gen - 1));
//                }
//            }
        }
        System.out.println("\ngenerations:   " + universe.getGeneration());
        System.out.println("cache size:    " + Block.internCache.size());

        System.out.println();

        long spentTime = System.currentTimeMillis() - startTile;
        System.out.printf("Spent time:         %,12d msec\n", spentTime);

        long usedMemoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long usedByUniverse = usedMemoryAfter - usedMemoryBefore;
        System.out.printf("Used memory after:  %,15d\nUsed by universe:   %,15d Bytes, %,11d KByte, %,7d MByte\n", usedMemoryAfter, usedByUniverse, usedByUniverse / 1024, usedByUniverse / 1024 / 1024);
        System.gc();
        System.gc();
        System.gc();
        long usedMemoryAfterAfterGC = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long usedByUniverseAfterGC = usedMemoryAfterAfterGC - usedMemoryBefore;
        System.out.printf("After GC\nUsed memory after:  %,15d\nUsed by universe:   %,15d Bytes, %,11d KByte, %,7d MByte\n", usedMemoryAfterAfterGC, usedByUniverseAfterGC, usedByUniverseAfterGC / 1024, usedByUniverseAfterGC / 1024 / 1024);


    }
}
