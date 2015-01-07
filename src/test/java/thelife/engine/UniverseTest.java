package thelife.engine;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UniverseTest {


    @Test
    public void addsLifeToFertileCell() {
        Space space = new Space();
        space.setLifeAt(new Point(1, 1));
        space.setLifeAt(new Point(1, 2));
        space.setLifeAt(new Point(1, 3));

        Universe universe = new Universe(space);
        universe.nextGeneration();

        assertThat(space.isLifeAt(new Point(0, 2)), is(true));
        assertThat(space.isLifeAt(new Point(2, 2)), is(true));

    }

    @Test
    public void removeLifeFrom() {
        Space space = new Space();
        space.setLifeAt(new Point(1, 1));
        space.setLifeAt(new Point(1, 2));
        space.setLifeAt(new Point(1, 3));

        Universe universe = new Universe(space);
        universe.nextGeneration();

        assertThat(space.isLifeAt(new Point(1, 1)), is(false));
        assertThat(space.isLifeAt(new Point(1, 3)), is(false));

    }

    @Test
    public void justRunRPentaminoEvolution(){
        Space space = new Space();
        Universe universe = new Universe(space);

        space.setLifeAt(new Point(0, 2));
        space.setLifeAt(new Point(1, 2));
        space.setLifeAt(new Point(0, 1));
        space.setLifeAt(new Point(-1, 1));
        space.setLifeAt(new Point(0, 0));


        for(int i = 0; i<1000; i++) {
            universe.nextGeneration();
        }
    }

}
