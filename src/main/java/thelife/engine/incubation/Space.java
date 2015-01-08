package thelife.engine.incubation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Space {

    private Set<Point> field = new HashSet<>();
    private Map<Point, Tile> tiles = new HashMap<>();

    public Space() {
    }

    public Space(Set<Point> aliveCells) {
        aliveCells.forEach(this::setLifeAt);
    }

    public void setLifeAt(Point point) {
        field.add(point);
        incLifeInTile(point);
        for (Point p : point.getNeighbors()) {
            incLifeInTile(p);
        }
    }

    public void removeLifeAt(Point point) {
        field.remove(point);
        decLifeInTile(point);
        for (Point p : point.getNeighbors()) {
            decLifeInTile(p);
        }
    }

    private void incLifeInTile(Point point) {
        getTile(point).inc();
    }

    private void decLifeInTile(Point point) {
        Tile tile = getTile(point);
        tile.dec();
        if(tile.getLifeCount() == 0) {
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
        return field.contains(point);
    }

    public boolean noLifeAt(Point point) {
        return !isLifeAt(point);
    }

    public Set<Point> getAllAliveCells() {
        return field;
    }

    public void clear() {
        field.clear();
    }

    public Map<Point, Tile> getTiles() {
        return tiles;
    }

}
