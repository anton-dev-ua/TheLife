package thelife.engine;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RleFormatterTest {

    @Test
    public void formatsCollectionOfPointsToRleString() throws Exception {
        Set<Point> points = new HashSet<>();

        points.add(new Point(0,-1));
        points.add(new Point(-1,0));
        points.add(new Point(0,0));
        points.add(new Point(0,1));
        points.add(new Point(1,1));

        String rleString = new RleFormatter().format(points);

        assertThat(rleString).isEqualTo("Pos=-1,-1 bo$2o$b2o!");
    }
}
