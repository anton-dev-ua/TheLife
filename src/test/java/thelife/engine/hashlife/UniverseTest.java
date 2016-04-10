package thelife.engine.hashlife;

import org.junit.Before;
import org.junit.Test;
import thelife.engine.Point;

import java.util.HashSet;
import java.util.Set;

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
}
