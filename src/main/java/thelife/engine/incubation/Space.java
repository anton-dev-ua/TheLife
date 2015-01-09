package thelife.engine.incubation;

import thelife.engine.Point;

import java.util.*;

public class Space {

    private Map<Point, Tile> tiles = new HashMap<>();

    public Space() {
    }

    public Space(Set<Point> aliveCells) {
        aliveCells.forEach(this::setLifeAt);
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
        if (tile.getLifeCount() == 0) {
            tiles.remove(point);
        } else {
            tile.setLifeInCenter(false);
        }
    }

    private void incLifeInTile(Point point) {
        Tile tile = getTile(point);
        tile.inc();
    }

    private void decLifeInTile(Point point) {
        Tile tile = getTile(point);
        tile.dec();
        if (tile.getLifeCount() == 0) {
            tiles.remove(point);
        }
    }

    private Tile getTile(Point point) {
        Tile tile = tiles.get(point);
        if (tile == null) {
            tile = new Tile();
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
}
