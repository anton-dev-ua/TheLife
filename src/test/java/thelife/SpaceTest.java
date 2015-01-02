package thelife;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SpaceTest {

    @Test
    public void keepsStateOfTheCell() {

        Space space = new Space();

        space.setLifeAt(2, 3);

        assertThat(space.isAliveAt(2, 3), is(true));

    }

    @Test
    public void noLifeByDefaultInTheCell(){
        Space space = new Space();

        assertThat(space.isAliveAt(4,5), is(false));
    }
}
