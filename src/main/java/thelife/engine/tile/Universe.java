package thelife.engine.tile;

import thelife.engine.Point;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Universe implements thelife.engine.Universe {
    private Space space;
    private int generation = 0;

    public Universe() {
        this.space = new Space();
    }

    @Override
    public void nextGeneration() {
        Set<Point> toBorn = new HashSet<>();
        Set<Point> toDie = new HashSet<>();

        Map<Point, Tile> tiles = space.getTiles();
//        System.out.printf("%6s: %s\n", generation+1, tiles);

        tiles.forEach((point, tile) -> {
            if (tile.getLifeCount() == 3) {
                if (tile.noLifeInCenter()) {
                    toBorn.add(point);
                }
            } else if (tile.getLifeCount() != 4) {
                if (tile.isLifeInCenter()) {
                    toDie.add(point);
                }
            }

        });


        toBorn.forEach(space::setLifeAt);
        toDie.forEach(space::removeLifeAt);

        generation++;
    }

    @Override
    public int getGeneration() {
        return generation;
    }

    @Override
    public void clear() {
        generation = 0;
        space.clear();
    }

    @Override
    public Collection<Point> getAllAliveCells() {
        return space.getAllAliveCells();
    }

    @Override
    public void setState(Collection<Point> state) {
        space.clear();
        state.forEach(space::setLifeAt);
    }
}
