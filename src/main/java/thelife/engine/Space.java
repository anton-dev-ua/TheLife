package thelife.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static thelife.engine.Utils.aNeighborPoints;

public class Space {

    private Map<Point, Boolean> field = new HashMap<>();

    public void setLifeAt(int x, int y) {
        field.put(new Point(x, y), true);
    }

    public boolean isLifeAt(Point point) {
        return field.get(point) == Boolean.TRUE;
    }

    public int getAliveNeighborsCountAt(Point point) {
        return aNeighborPoints().stream().mapToInt((neighbor) -> isLifeAt(point.add(neighbor)) ? 1 : 0).sum();
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
