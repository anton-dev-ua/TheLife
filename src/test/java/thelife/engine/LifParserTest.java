package thelife.engine;


import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class LifParserTest {

    @Test
    public void parserLifStringWithO() throws Exception {
        Collection<Point> parse = new LifParser().parse("" +
                ".OO.\n" +
                "O..O\n" +
                ".O.O\n" +
                "..O.");

        assertThat(parse).containsOnly(p(-1, 1), p(0, 1), p(-2, 0), p(1, 0), p(-1, -1), p(1, -1), p(0, -2));

    }

    @Test
    public void parserLifStringWithStar() throws Exception {
        Collection<Point> parse = new LifParser().parse("" +
                ".**.\n" +
                "*..*\n" +
                ".*.*\n" +
                "..*.");

        assertThat(parse).containsOnly(p(-1, 1), p(0, 1), p(-2, 0), p(1, 0), p(-1, -1), p(1, -1), p(0, -2));

    }

    private Point p(int x, int y) {
        return new Point(x, y);
    }
}