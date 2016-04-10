package thelife.engine.hashlife;


import org.junit.Before;
import org.junit.Test;
import thelife.engine.Point;

import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockTest {
    
    @Before
    public void before() {
//        Block.cleanInternalCache();
    }

    @Test
    public void createsEmptySpace() throws Exception {

        Block space = Block.create(5);

    }

    @Test
    public void initsAllEmpty() throws Exception {
        Block space = Block.create(4);

        assertThat(space.topLeft.bottomLeft.topRight.topRight.population).isEqualTo(0);

    }

    @Test
    public void savesCellState() throws Exception {
        Block space = Block.create(4);
        space = space.setLifeAt(-5, 3);

        assertThat(space.topLeft.bottomLeft.topRight.topRight.population).isEqualTo(1);

    }

    @Test
    public void calculatesNextGenForLevel2() {
        Block space = Block.create(2)
                .setLifeAt(-1, -1)
                .setLifeAt(0, -2)
                .setLifeAt(1, -1);

        Block nextGen = space.nextGen();

        assertThat(new int[]{nextGen.topLeft.population, nextGen.topRight.population, nextGen.bottomLeft.population, nextGen.bottomRight.population})
                .isEqualTo(new int[]{0, 0, 0, 1});
    }

    @Test
    public void calculatesNextGenForLevel2VerticalPipeAtRightEdge() {
        Block space = Block.create(2)
                .setLifeAt(1, -1)
                .setLifeAt(1, 0)
                .setLifeAt(1, 2);

        Block nextGen = space.nextGen();

        assertThat(new int[]{nextGen.topLeft.population, nextGen.topRight.population, nextGen.bottomLeft.population, nextGen.bottomRight.population})
                .isEqualTo(new int[]{0, 1, 0, 0});
    }

    @Test
    public void calculatesNextGenForLevel2VerticalPipeAtBottomMiddle() {
        Block space = Block.create(2)
                .setLifeAt(0, 0)
                .setLifeAt(0, -1)
                .setLifeAt(0, -2);

        Block nextGen = space.nextGen();


        assertThat(new int[]{nextGen.topLeft.population, nextGen.topRight.population, nextGen.bottomLeft.population, nextGen.bottomRight.population})
                .isEqualTo(new int[]{0, 0, 1, 1});


    }

    @Test
    public void calculatesNextGenForLevel2VerticalPipeAtLeftEdge() {
        Block space = Block.create(2)
                .setLifeAt(-2, -1)
                .setLifeAt(-2, 0)
                .setLifeAt(-2, 2);

        Block nextGen = space.nextGen();

        assertThat(new int[]{nextGen.topLeft.population, nextGen.topRight.population, nextGen.bottomLeft.population, nextGen.bottomRight.population})
                .isEqualTo(new int[]{1, 0, 0, 0});
    }

    @Test
    public void calculatesNextGenForLevel4VerticalPipeAtRightEdge() {
        Block space = Block.create(4)
                .setLifeAt(1, 1)
                .setLifeAt(1, 2)
                .setLifeAt(1, 3);

        Block nextGen = space.nextGen();

        assertThat(nextGen.getAllAliveCells()).hasSize(3).contains(new Point(0, 2), new Point(1, 2), new Point(2, 2));
    }

    @Test
    public void calculatesNextGenForLevel3() {
        Block space = Block.create(3)
                .setLifeAt(1, 2)
                .setLifeAt(2, 3)
                .setLifeAt(2, 1)
                .setLifeAt(-3, 0)
                .setLifeAt(-2, 0)
                .setLifeAt(-3, -1)
                .setLifeAt(-1, -2);

        Block nextGen = space.expand().nextGen();

        assertThat(nextGen.topLeft.bottomLeft.population).isEqualTo(1);
        assertThat(nextGen.topRight.topRight.population).isEqualTo(1);
        assertThat(nextGen.bottomLeft.bottomRight.population).isEqualTo(0);
    }

    @Test
    public void returnsAllAliveCells() {
        Block space = Block.create(3)
                .setLifeAt(1, 2)
                .setLifeAt(2, 3)
                .setLifeAt(2, 1)
                .setLifeAt(-3, 0)
                .setLifeAt(-2, 0)
                .setLifeAt(-3, -1)
                .setLifeAt(-1, -2);


        Collection<Point> expected = new HashSet<>();
        expected.add(new Point(1, 2));
        expected.add(new Point(2, 3));
        expected.add(new Point(2, 1));
        expected.add(new Point(-3, 0));
        expected.add(new Point(-2, 0));
        expected.add(new Point(-3, -1));
        expected.add(new Point(-1, -2));


        assertThat(space.getAllAliveCells()).hasSameSizeAs(expected).containsAll(expected);

    }

}