package thelife.engine.point;

import thelife.engine.Point;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static thelife.engine.Utils.aNeighborPoints;

public class Universe implements thelife.engine.Universe {
    private Space space;
    private long generation = 0;
    private Set<Point> nextCellsToCheck;

    public Universe() {
        this.space = new Space();
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

    public BigInteger getGeneration() {
        return BigInteger.valueOf(generation);
    }

    public void clear() {
        generation = 0;
        space.clear();
    }

    @Override
    public Collection<Point> getAllAliveCells() {
        return space.getAllAliveCells();
    }

    @Override
    public void setState(Collection<Point> points) {
        space.clear();
        points.forEach(space::setLifeAt);
    }

    @Override
    public void addLife(Point point) {
        space.setLifeAt(point);
    }

    @Override
    public void removeLife(Point point) {
        space.removeLifeAt(point);
    }
}
