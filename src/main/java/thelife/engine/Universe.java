package thelife.engine;

import java.util.HashSet;
import java.util.Set;

import static thelife.engine.Utils.aNeighborPoints;

public class Universe {
    private Space space;
    private int generation = 0;

    public Universe(Space space) {

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
            for (Point neighborDelta : aNeighborPoints()) {
                Point neighbor = point.add(neighborDelta);
                if (space.noLifeAt(neighbor) && ruleToBorn(neighbor)) {
                    toBorn.add(neighbor);
                }
            }
        }

        return toBorn;
    }

    private boolean ruleToBorn(Point neighbor) {
        return 3 == space.getAliveNeighborsCountAt(neighbor);
    }

    public int getGeneration() {
        return generation;
    }

    public void clear() {
        generation = 0;
        space.clear();
    }
}