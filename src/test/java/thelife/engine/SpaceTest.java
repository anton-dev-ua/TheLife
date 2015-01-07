package thelife.engine;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SpaceTest {

    private Space space;

    @Before
    public void setUp() {
        space = new Space();
    }

    @Test
    public void noLifeByDefaultInTheCell() {
        assertThat(space.isLifeAt(new Point(4, 5)), is(false));
    }

    @Test
    public void keepsStateOfTheCell() {
        space.setLifeAt(new Point(2, 3));

        assertThat(space.isLifeAt(new Point(2, 3)), is(true));
    }

    @Test
    public void removesLife() {
        space.setLifeAt(new Point(2, 3));
        space.removeLifeAt(new Point(2, 3));

        assertThat(space.isLifeAt(new Point(2, 3)), is(false));
    }

    @Test
    public void calculatesCountOfNeighborLife() {
        space.setLifeAt(new Point(2, 3));
        space.setLifeAt(new Point(2, 4));
        space.setLifeAt(new Point(1, 3));

        assertThat(space.getAliveNeighborsCountAt(new Point(1, 4)), is(3));
    }

}
