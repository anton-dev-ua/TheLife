package thelife.engine.tile_v2;

import thelife.engine.Point;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

public class Universe implements thelife.engine.Universe {
    private Space space;
    private long generation = 0;

    public Universe() {
        this.space = new Space();
    }

    @Override
    public void nextGeneration() {
        int evenOrOdd = (int) (generation % 2);

        List<Tile> changedTiles = space.getChangedTiles();
        changedTiles.forEach(tile -> {
            if (tile.getChangedInGeneration(evenOrOdd) == 1) {
                space.setLifeAt(tile.getPoint());
            } else if (tile.getChangedInGeneration(evenOrOdd) == 2) {
                space.removeLifeAt(tile.getPoint());
            }

            tile.setChangedInGeneration(evenOrOdd, 0);
        });

        changedTiles.clear();

        generation++;
    }

    @Override
    public BigInteger getGeneration() {
        return BigInteger.valueOf(generation);
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
