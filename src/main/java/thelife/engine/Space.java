package thelife.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.Boolean.TRUE;
import static thelife.engine.Utils.aNeighborPoints;

public class Space {

    private Map<Point, Boolean> field = new HashMap<>();

    public void setLifeAt(int x, int y) {
        field.put(new Point(x, y), TRUE);
    }

    public boolean isLifeAt(Point point) {
        return field.get(point) == TRUE;
    }

    public boolean noLifeAt(Point point) {
        return !isLifeAt(point);
    }

    public int getAliveNeighborsCountAt(Point point) {
        int count = 0;
        for (Point neighborDelta : aNeighborPoints()) {
            if (isLifeAt(point.add(neighborDelta))) {
                count++;
            }
        }
        return count;
    }

    public void removeLifeAt(int x, int y) {
        field.remove(new Point(x, y));
    }

    public Set<Point> getAllAliveCells() {
        return field.keySet();
    }

    public void clear() {
        field.clear();
    }
}
