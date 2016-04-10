package thelife.engine.hashlife;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;
import thelife.engine.Point;
import thelife.engine.RleParser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class UniverseTest {


    private Set<Point> initialState;

    @Before
    public void setUp() throws Exception {
        Block.cleanInternalCache();
        initialState = new HashSet<>();
        initialState.add(new Point(1, 1));
        initialState.add(new Point(1, 2));
        initialState.add(new Point(1, 3));
    }

    @Test
    public void addsLifeToFertileCell() {
        thelife.engine.Universe universe = new CachedQuadTreeUniverse();
        universe.setState(initialState);

        universe.nextGeneration();

        System.out.println(universe.getAllAliveCells());
        assertThat(universe.getAllAliveCells()).contains(new Point(0, 2), new Point(2, 2));

    }

    @Test
    public void removeLifeFrom() {
        thelife.engine.Universe universe = new CachedQuadTreeUniverse();
        universe.setState(initialState);

        universe.nextGeneration();

        assertThat(universe.getAllAliveCells()).doesNotContain(new Point(1, 1), new Point(1, 3));

    }

    @Test
    public void performanceOfGettingCellsInfo() {
        Universe universe = new Universe();
        RleParser rleParser = new RleParser();

        universe.setState(rleParser.parse("Pos=-1,-1 bo$2o$b2o!"));
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        IntStream.range(1, 17).forEach(i -> universe.nextGeneration());
        stopWatch.stop();
        
        System.out.printf("simulate life - time elapsed: %s\n", stopWatch);
        System.out.printf("pop: %s, gen: %s, level: %s\n\n", universe.getPopulation(), universe.getGeneration(), universe.getLevel());
        
        
        stopWatch.reset();
        stopWatch.start();
        Collection<Point> allAliveCells = universe.getAllAliveCells();
        stopWatch.stop();

        System.out.printf("get alive cells - time elapsed: %s\n", stopWatch);
        System.out.printf("%s", allAliveCells);
    }
}
