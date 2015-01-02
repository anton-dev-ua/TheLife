package thelife;

import java.util.HashSet;
import java.util.Set;

public class World {
    private Space space;
    int generation = 0;

    public World(Space space) {

        this.space = space;
    }

    public void nextGeneration() {
        Set<Space.CellKey> toBorn = findCellsToBorn();
        Set<Space.CellKey> toDie = findCellsToDie();

        for (Space.CellKey cellKey : toBorn) {
            space.setLifeAt(cellKey.getX(), cellKey.getY());
        }

        for(Space.CellKey cellKey: toDie) {
            space.removeLifeAt(cellKey.getX(), cellKey.getY());
        }

//        System.out.printf("Generation: %5s, lives:%s\n", ++generation, space.getAllAliveCells().size());

    }

    private Set<Space.CellKey> findCellsToDie() {
        Set<Space.CellKey> allAliveCells = space.getAllAliveCells();

        Set<Space.CellKey> toDie = new HashSet<>();

        for (Space.CellKey cellKey: allAliveCells) {
            int aliveNeighbors = space.getAliveNeighborsCountAt(cellKey.getX(), cellKey.getY());
            if(aliveNeighbors != 2 && aliveNeighbors !=3) {
                toDie.add(cellKey);
            }
        }

        return toDie;
    }

    private Set<Space.CellKey> findCellsToBorn() {
        Set<Space.CellKey> allAliveCells = space.getAllAliveCells();

        Set<Space.CellKey> toBorn = new HashSet<>();

        for (Space.CellKey cellKey : allAliveCells) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (!space.isLifeAt(cellKey.getX() + x, cellKey.getY() + y)) {
                        int aliveNeighbors = space.getAliveNeighborsCountAt(cellKey.getX() + x, cellKey.getY() + y);
                        if (aliveNeighbors == 3) {
                            toBorn.add(new Space.CellKey(cellKey.getX() + x, cellKey.getY() + y));
                        }
                    }
                }
            }
        }
        return toBorn;
    }
}
