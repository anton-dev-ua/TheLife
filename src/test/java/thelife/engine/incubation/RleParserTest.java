package thelife.engine.incubation;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RleParserTest {


    private Set<Point> expected;
    private RleParser rleParser;

    @Before
    public void setUp() throws Exception {
        expected = new HashSet<>();
        rleParser = new RleParser();

    }

    @Test
    public void parsesRlePattern() throws Exception {
        addPoint(0, -1);
        addPoint(-1, 0);
        addPoint(0, 0);
        addPoint(0, 1);
        addPoint(1, 1);

        Set<Point> cells = rleParser.parse("Pos=-1,-1 bo$2o$b2o!");

        assertThat(cells).isEqualTo(expected);

    }

    @Test
    public void parsesPatternWithemptyLines() throws Exception {

        addPoint(0, -4);
        addPoint(0, 1);

        Set<Point> cells = rleParser.parse("Pos=-1,-4 bo5$bo!");

        assertThat(cells).isEqualTo(expected);

    }

    @Test
    public void parsesRlePatternWithLeadingEmptyLines() throws Exception {
        addPoint(0, 1);

        Set<Point> cells = rleParser.parse("Pos=-1,-4 5$bo!");

        assertThat(cells).isEqualTo(expected);
    }

    private boolean addPoint(int x, int y) {
        return expected.add(new Point(x, y));
    }
}
