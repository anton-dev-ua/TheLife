package thelife.engine;

import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UniverseSmokeTest {

    private RleParser rleParser = new RleParser();

    @Test
    public void rPentaminoEvolution() throws Exception {
        Set<Point> initialState = rleParser.parse("Pos=-1,-1 bo$2o$b2o!");

        Space space = new Space(initialState);
        Universe universe = new Universe(space);

        assertThat(space.getAllAliveCells()).isEqualTo(initialState);

        universe.nextGeneration();
        assertThat(space.getAllAliveCells()).as("generation 1").isEqualTo(rleParser.parse("Pos=-1,-1 2o$o$3o!"));

        universe.nextGeneration();
        assertThat(space.getAllAliveCells()).as("generation 2").isEqualTo(rleParser.parse("Pos=-2,-1 b2o$o2bo$b2o$2bo!"));

        universe.nextGeneration();
        assertThat(space.getAllAliveCells()).as("generation 3").isEqualTo(rleParser.parse("Pos=-2,-1 b2o$o2bo$b3o$b2o!"));

        universe.nextGeneration();
        assertThat(space.getAllAliveCells()).as("generation 4").isEqualTo(rleParser.parse("Pos=-2,-1 b2o$o2bo$o2bo$bobo!"));
    }
}
