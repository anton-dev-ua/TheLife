package thelife.engine.incubation;

import java.util.HashSet;
import java.util.Set;

import static thelife.engine.incubation.Utils.aNeighborPoints;

public class Universe {
    private Space space;
    private int generation = 0;

    public Universe(Space space) {

        this.space = space;
    }

    public void nextGeneration() {
        Set<Point> toBorn = findCellsToBorn();
        Set<Point> toDie = findCellsToDie();

        toBorn.forEach(space::setLifeAt);
        toDie.forEach(space::removeLifeAt);

        generation++;
    }

    private Set<Point> findCellsToDie() {
        Set<Point> toDie = new HashSet<>();

        for (Point point : space.getAllAliveCells()) {
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
