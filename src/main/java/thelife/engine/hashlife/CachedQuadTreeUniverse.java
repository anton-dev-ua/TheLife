package thelife.engine.hashlife;

import thelife.engine.Point;

import java.math.BigInteger;
import java.util.Collection;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class CachedQuadTreeUniverse implements thelife.engine.Universe {
    private Block space;
    private BigInteger generation = BigInteger.ZERO;

    public CachedQuadTreeUniverse() {
        this.space = createInitialBlock();
    }

    protected Block createInitialBlock() {
        return Block.create(3);
    }

    @Override
    public void nextGeneration() {

        while (space.topLeft.population != space.topLeft.bottomRight.bottomRight.population ||
                space.topRight.population != space.topRight.bottomLeft.bottomLeft.population ||
                space.bottomLeft.population != space.bottomLeft.topRight.topRight.population ||
                space.bottomRight.population != space.bottomRight.topLeft.topLeft.population) {
            space = space.expand();
        }
        int level = space.level;
        space = space.nextGen();
        generation = incGeneration(generation, level);
    }

    protected BigInteger incGeneration(BigInteger generation, int level) {
        return generation.add(BigInteger.ONE);
    }

    @Override
    public BigInteger getGeneration() {
        return generation;
    }

    @Override
    public void clear() {
        generation = BigInteger.ZERO;
        this.space = createInitialBlock();
    }

    @Override
    public Collection<Point> getAllAliveCells() {
        return space.getAllAliveCells();
    }

    @Override
    public void setState(Collection<Point> state) {

        int max = state.stream().mapToInt(point -> max(abs(point.getX()), abs(point.getY()))).max().getAsInt();
        while (1 << (space.level - 1) < max) {
            space = space.expand();
        }

        for (Point point : state) {
            space = space.setLifeAt(point.getX(), point.getY(), true);
        }
    }

    @Override
    public void addLife(Point point) {
        int max = max(abs(point.getX()), abs(point.getY()));
        while (1 << (space.level - 1) < max) {
            space = space.expand();
        }
        space = space.setLifeAt(point.getX(), point.getY(), true);
    }

    @Override
    public void removeLife(Point point) {
        int max = max(abs(point.getX()), abs(point.getY()));
        if (1 << (space.level - 1) >= max) {
            space = space.setLifeAt(point.getX(), point.getY(), false);
        }
    }

    public int getLevel() {
        return space.level;
    }

    public int getPopulation() {
        return space.getPopulation();
    }
}
