package thelife.engine;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SpaceTest {

    private Space space;

    @Before
    public void setUp() {
        space = new Space();
    }

    @Test
    public void noLifeByDefaultInTheCell() {
        assertThat(space.isLifeAt(new Point(4, 5))).isFalse();
    }

    @Test
    public void keepsStateOfTheCell() {
        space.setLifeAt(new Point(2, 3));

        assertThat(space.isLifeAt(new Point(2, 3))).isTrue();
    }

    @Test
    public void removesLife() {
        space.setLifeAt(new Point(2, 3));
        space.removeLifeAt(new Point(2, 3));

        assertThat(space.isLifeAt(new Point(2, 3))).isFalse();
    }

    @Test
    public void addsAllTilesWhenSettingLife() {
        space.setLifeAt(new Point(0, 0));

        assertThat(space.getTiles()).hasSize(9);
    }

    @Test
    public void addsTilesAroundPoint() {
        space.setLifeAt(new Point(0, 0));

        assertThat(space.getTiles()).containsKeys(
                new Point(-1, -1),
                new Point(+0, -1),
                new Point(+1, -1),
                new Point(-1, +0),
                new Point(+0, +0),
                new Point(+1, +0),
                new Point(-1, +1),
                new Point(+0, +1),
                new Point(+1, +1)
        );
    }

    @Test
    public void incrementsLifeCountInNewlyAddedTile() {
        space.setLifeAt(new Point(0, 0));

        space.getTiles().forEach((point, tile) -> assertThat(tile.getLifeCount()).isEqualTo(1));
    }

    @Test
    public void incrementsLifeInExistingTileWhenSettingLife() {

        space.setLifeAt(new Point(0, 0));
        space.setLifeAt(new Point(0, 1));

        Map<Point, Tile> tiles = space.getTiles();
        assertThat(tiles.get(new Point(0, 0)).getLifeCount()).isEqualTo(2);
        assertThat(tiles.get(new Point(0, 1)).getLifeCount()).isEqualTo(2);
        assertThat(tiles.get(new Point(1, 1)).getLifeCount()).isEqualTo(2);
        assertThat(tiles.get(new Point(1, 0)).getLifeCount()).isEqualTo(2);
        assertThat(tiles.get(new Point(-1, 1)).getLifeCount()).isEqualTo(2);
        assertThat(tiles.get(new Point(-1, 0)).getLifeCount()).isEqualTo(2);

    }

    @Test
    public void setsTrueForLifeInCenter() throws Exception {
        space.setLifeAt(new Point(0, 0));

        assertThat(space.getTiles().get(new Point(0, 0)).isLifeInCenter()).isTrue();
    }

    @Test
    public void setsFalseForLifeInCenter() throws Exception {
        space.setLifeAt(new Point(0, 0));
        space.setLifeAt(new Point(1, 0));
        space.removeLifeAt(new Point(0, 0));

        assertThat(space.getTiles().get(new Point(0, 0)).noLifeInCenter()).isTrue();
    }
}
