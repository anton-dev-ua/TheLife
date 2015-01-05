package thelife;

import java.util.HashSet;
import java.util.Set;

import static thelife.Utils.aNeighborPoints;

public class World {
    private Space space;
    private int generation = 0;

    public World(Space space) {

        this.space = space;
    }

    public void nextGeneration() {
        Set<Point> toBorn = findCellsToBorn();
        Set<Point> toDie = findCellsToDie();

        for (Point cellKey : toBorn) {
            space.setLifeAt(cellKey.getX(), cellKey.getY());
        }

        for (Point cellKey : toDie) {
            space.removeLifeAt(cellKey.getX(), cellKey.getY());
        }

        generation++;
    }

    private Set<Point> findCellsToDie() {
        Set<Point> toDie = new HashSet<>();

        for (Point point : space.getAllAliveCells()) {
            int aliveNeighbors = space.getAliveNeighborsCountAt(point);
            if (aliveNeighbors != 2 && aliveNeighbors != 3) {
                toDie.add(point);
            }
        }

        return toDie;
    }

    private Set<Point> findCellsToBorn() {
        Set<Point> toBorn = new HashSet<>();

        for (Point point : space.getAllAliveCells()) {
            aNeighborPoints().forEach((neighborDelta) -> {
                Point neighbor = point.add(neighborDelta);
                if (!space.isLifeAt(neighbor)) {
                    if (3 == space.getAliveNeighborsCountAt(neighbor)) {
                        toBorn.add(neighbor);
                    }
                }
            });
        }

        return toBorn;
    }

    public int getGeneration() {
        return generation;
    }

    public void clear() {
        generation = 0;
        space.clear();
    }
}
