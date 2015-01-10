package thelife.engine.tile_v2;

import thelife.engine.Point;

import java.util.*;

public class Space {

    private Map<Point, Tile> tiles = new HashMap<>();
    private List<List<Tile>> changedTiles = new ArrayList<>();
    private List<Tile> curChangedTiles;


    int evenOrOdd = 0;

    public Space() {
        changedTiles.add(new ArrayList<>(100));
        changedTiles.add(new ArrayList<>(100));
        curChangedTiles = changedTiles.get(evenOrOdd);
    }

    public void setLifeAt(Point point) {
        incLifeInTileCenter(point);
        for (Point p : point.getNeighbors()) {
            incLifeInTile(p);
        }
    }

    private void incLifeInTileCenter(Point point) {
        Tile tile = getTile(point);
        tile.setLifeInCenter(true);
        tile.inc();
        tileChanged(tile);
    }

    private void tileChanged(Tile tile) {
        if (tile.getChangedInGeneration(evenOrOdd) == 0) {
            if (tile.noLifeInCenter() && tile.getLifeCount() == 3) {
                tile.setChangedInGeneration(evenOrOdd, 1);
                curChangedTiles.add(tile);
            } else if (tile.isLifeInCenter() && tile.getLifeCount() != 3 && tile.getLifeCount() != 4) {
                tile.setChangedInGeneration(evenOrOdd, 2);
                curChangedTiles.add(tile);
            }

        } else {
            if (tile.noLifeInCenter() && tile.getLifeCount() == 3) {
                tile.setChangedInGeneration(evenOrOdd, 1);
            } else if (tile.isLifeInCenter() && tile.getLifeCount() != 3 && tile.getLifeCount() != 4) {
                tile.setChangedInGeneration(evenOrOdd, 2);
            } else {
                tile.setChangedInGeneration(evenOrOdd, 3);
            }
        }

    }

    public void removeLifeAt(Point point) {
        decLifeInTileCenter(point);
        for (Point p : point.getNeighbors()) {
            decLifeInTile(p);
        }
    }

    private void decLifeInTileCenter(Point point) {
        Tile tile = getTile(point);
        tile.dec();
        tile.setLifeInCenter(false);
        if (tile.getLifeCount() == 0) {
            tiles.remove(point);
        }
        tileChanged(tile);
    }

    private void incLifeInTile(Point point) {
        Tile tile = getTile(point);
        tile.inc();
        tileChanged(tile);
    }

    private void decLifeInTile(Point point) {
        Tile tile = getTile(point);
        tile.dec();
        if (tile.getLifeCount() == 0) {
            tiles.remove(point);
        } else {
            tileChanged(tile);
        }

    }

    private Tile getTile(Point point) {
        Tile tile = tiles.get(point);
        if (tile == null) {
            tile = new Tile(point);
            tiles.put(point, tile);
        }
        return tile;
    }

    public boolean isLifeAt(Point point) {
        Tile tile = tiles.get(point);
        return tile != null && tile.isLifeInCenter();
    }

    public boolean noLifeAt(Point point) {
        return !isLifeAt(point);
    }

    public Collection<Point> getAllAliveCells() {
        Set<Point> lives = new HashSet<>();
        tiles.forEach((point, tile) -> {
            if (tile.isLifeInCenter()) {
                lives.add(point);
            }
        });

        return lives;
    }

    public void clear() {
        tiles.clear();
    }

    public Map<Point, Tile> getTiles() {
        return tiles;
    }

    public List<Tile> getChangedTiles() {
        List<Tile> toReturn = curChangedTiles;
        evenOrOdd = evenOrOdd == 0 ? 1 : 0;
        curChangedTiles = changedTiles.get(evenOrOdd);
        return toReturn;
    }
}
