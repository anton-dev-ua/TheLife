package thelife.engine;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final List<Point> neighborPoints = new ArrayList<>();
    static {
        neighborPoints.add(new Point(-1, -1));
        neighborPoints.add(new Point(-1, 0));
        neighborPoints.add(new Point(-1, 1));
        neighborPoints.add(new Point(0, -1));
        neighborPoints.add(new Point(0, 1));
        neighborPoints.add(new Point(1, -1));
        neighborPoints.add(new Point(1, 0));
        neighborPoints.add(new Point(1, 1));
    }

    public static List<Point> aNeighborPoints() {
        return neighborPoints;
    }

}
