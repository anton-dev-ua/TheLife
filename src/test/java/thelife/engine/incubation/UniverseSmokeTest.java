package thelife.engine.incubation;

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

        assertThat(space.getAllAliveCells()).as("initial state").isEqualTo(initialState);

        universe.nextGeneration();
        assertThat(space.getAllAliveCells()).as("generation "+universe.getGeneration()).isEqualTo(rleParser.parse("Pos=-1,-1 2o$o$3o!"));

        universe.nextGeneration();
        assertThat(space.getAllAliveCells()).as("generation "+universe.getGeneration()).isEqualTo(rleParser.parse("Pos=-2,-1 b2o$o2bo$b2o$2bo!"));

        universe.nextGeneration();
        assertThat(space.getAllAliveCells()).as("generation "+universe.getGeneration()).isEqualTo(rleParser.parse("Pos=-2,-1 b2o$o2bo$b3o$b2o!"));

        universe.nextGeneration();
        assertThat(space.getAllAliveCells()).as("generation "+universe.getGeneration()).isEqualTo(rleParser.parse("Pos=-2,-1 b2o$o2bo$o2bo$bobo!"));

        universe.nextGeneration();
        assertThat(space.getAllAliveCells()).as("generation "+universe.getGeneration()).isEqualTo(rleParser.parse("Pos=-2,-1 b2o$o2bo$2ob2o$2bo!"));

        universe.nextGeneration();
        assertThat(space.getAllAliveCells()).as("generation "+universe.getGeneration()).isEqualTo(rleParser.parse("Pos=-2,-1 b2o$o2b2o$2ob2o$b3o!"));

        universe.nextGeneration();
        assertThat(space.getAllAliveCells()).as("generation "+universe.getGeneration()).isEqualTo(rleParser.parse("Pos=-2,-1 b3o$o3bo$o$2ob2o$2bo!"));

    }
}
