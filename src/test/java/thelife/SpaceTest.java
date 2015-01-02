package thelife;

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
        assertThat(space.isAliveAt(4, 5), is(false));
    }

    @Test
    public void keepsStateOfTheCell() {
        space.setLifeAt(2, 3);

        assertThat(space.isAliveAt(2, 3), is(true));
    }

    @Test
    public void calculatesCountOfNeighborLife() {
        space.setLifeAt(2, 3);
        space.setLifeAt(2, 4);
        space.setLifeAt(1, 3);

        assertThat(space.getAliveNeighborsCountAt(1, 4), is(3));
    }
}
