package thelife.engine.point;

import thelife.engine.Point;

import java.util.HashSet;
import java.util.Set;

import static thelife.engine.Utils.aNeighborPoints;

public class Space {

    private Set<Point> field = new HashSet<>();

    public Space() {
    }

    public Space(Set<Point> field) {
        this.field.addAll(field);
    }

    public void setLifeAt(Point point) {
        field.add(point);
    }

    public boolean isLifeAt(Point point) {
        return field.contains(point);
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

    public void removeLifeAt(Point point) {
        field.remove(point);
    }

    public Set<Point> getAllAliveCells() {
        return field;
    }

    public void clear() {
        field.clear();
    }
}
