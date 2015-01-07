package thelife.engine;

import java.util.HashSet;
import java.util.Set;

import static thelife.engine.Utils.aNeighborPoints;

public class Universe {
    private Space space;
    private int generation = 0;
    private Set<Point> nextCellsToCheck;

    public Universe(Space space) {
        this.space = space;
    }

    public void nextGeneration() {
        Set<Point> toBorn = findCellsToBorn();
        Set<Point> toDie = findCellsToDie();

        setNextCellsToCheck(toBorn, toDie);

        toBorn.forEach(space::setLifeAt);
        toDie.forEach(space::removeLifeAt);

        generation++;
    }

    private void setNextCellsToCheck(Set<Point> toBorn, Set<Point> toDie) {
        nextCellsToCheck = new HashSet<>();
        nextCellsToCheck.addAll(toBorn);
        nextCellsToCheck.addAll(toDie);
    }

    private Set<Point> findCellsToDie() {
        Set<Point> toDie = new HashSet<>();

        Set<Point> cellsToCheck = space.getAllAliveCells();

        for (Point point : cellsToCheck) {
            int aliveNeighbors = space.getAliveNeighborsCountAt(point);
            if (ruleToDie(aliveNeighbors)) {
                toDie.add(point);
            }
        }

        return toDie;
    }

    private boolean ruleToDie(int aliveNeighbors) {
        return aliveNeighbors != 2 && aliveNeighbors != 3;
    }

    private Set<Point> findCellsToBorn() {
        Set<Point> toBorn = new HashSet<>();

        for (Point point : getCellsToCheck()) {
            for (Point neighborDelta : aNeighborPoints()) {
                Point neighbor = point.add(neighborDelta);
                if (space.noLifeAt(neighbor) && ruleToBorn(neighbor)) {
                    toBorn.add(neighbor);
                }
            }
        }

        return toBorn;
    }

    private Set<Point> getCellsToCheck() {
        return generation > 0 ? nextCellsToCheck : space.getAllAliveCells();
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
