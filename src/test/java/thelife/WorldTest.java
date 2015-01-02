package thelife;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WorldTest {


    @Test
    public void addsLifeToFertileCell() {
        Space space = new Space();
        space.setLifeAt(1, 1);
        space.setLifeAt(1, 2);
        space.setLifeAt(1, 3);

        World world = new World(space);
        world.nextGeneration();

        assertThat(space.isLifeAt(new Point(0, 2)), is(true));
        assertThat(space.isLifeAt(new Point(2, 2)), is(true));

    }

    @Test
    public void removeLifeFrom() {
        Space space = new Space();
        space.setLifeAt(1, 1);
        space.setLifeAt(1, 2);
        space.setLifeAt(1, 3);

        World world = new World(space);
        world.nextGeneration();

        assertThat(space.isLifeAt(new Point(1, 1)), is(false));
        assertThat(space.isLifeAt(new Point(1, 3)), is(false));

    }

}
