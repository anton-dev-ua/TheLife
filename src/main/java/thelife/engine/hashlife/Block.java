package thelife.engine.hashlife;

import thelife.engine.Point;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class Block {

    private static Map<Block, Block> internCache = new HashMap<>();

    Block topLeft, topRight, bottomLeft, bottomRight;
    int level;
    private boolean alive = false;
    int population = 0;
    private Block cachedResult;
    private Collection<Point> cachedPoints;

    Block(int aLevel) {
        level = aLevel;
        if (level > 0) {
            Block empty = createBlock(level - 1);
            topLeft = empty;
            topRight = empty;
            bottomLeft = empty;
            bottomRight = empty;
        }
    }

    Block(int aLevel, Block topLeft, Block topRight, Block bottomLeft, Block bottomRight) {
        level = aLevel;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        population = this.topLeft.population + this.topRight.population + this.bottomLeft.population + this.bottomRight.population;
    }

    Block(boolean alive) {
        this.alive = alive;
        if (alive) population = 1;
    }

    Block createBlock(int aLevel) {
        return new Block(aLevel).intern();
    }

    Block createBlock(int aLevel, Block topLeft, Block topRight, Block bottomLeft, Block bottomRight) {
        return new Block(aLevel, topLeft, topRight, bottomLeft, bottomRight).intern();
    }

    Block createBlock(boolean alive) {
        return new Block(alive).intern();
    }

    static Block create(int level) {
        return new Block(level).intern();
    }

    public Block nextGen() {
        if (cachedResult == null) {
            cachedResult = calcNextGen();
        }
        return cachedResult;
    }

    private Block calcNextGen() {
        if (level < 2) {
            throw new RuntimeException("Block too small to calculate next generation");
        }

        if (population == 0) return topLeft;

        if (level == 2) {
            return calculateLife();
        } else {

            Block b00 = topLeft.centerBlock();
            Block b10 = horizontal(topLeft, topRight);
            Block b20 = topRight.centerBlock();
            Block b01 = vertical(topLeft, bottomLeft);
            Block b11 = centerSubBlock();
            Block b21 = vertical(topRight, bottomRight);
            Block b02 = bottomLeft.centerBlock();
            Block b12 = horizontal(bottomLeft, bottomRight);
            Block b22 = bottomRight.centerBlock();

            return createBlock(
                    level - 1,
                    createBlock(level - 1, b00, b10, b01, b11).nextGen(),
                    createBlock(level - 1, b10, b20, b11, b21).nextGen(),
                    createBlock(level - 1, b01, b11, b02, b12).nextGen(),
                    createBlock(level - 1, b11, b21, b12, b22).nextGen()
            );
        }
    }

    protected Block centerSubBlock() {
        return createBlock(level - 2, topLeft.bottomRight.bottomRight, topRight.bottomLeft.bottomLeft, bottomLeft.topRight.topRight, bottomRight.topLeft.topLeft);
    }

    protected Block vertical(Block top, Block bottom) {
        return createBlock(level - 2, top.bottomLeft.bottomRight, top.bottomRight.bottomLeft, bottom.topLeft.topRight, bottom.topRight.topLeft);
    }

    protected Block horizontal(Block left, Block right) {
        return createBlock(level - 2, left.topRight.bottomRight, right.topLeft.bottomLeft, left.bottomRight.topRight, right.bottomLeft.topLeft);
    }

    protected Block centerBlock() {
        return createBlock(level - 1, topLeft.bottomRight, topRight.bottomLeft, bottomLeft.topRight, bottomRight.topLeft);
    }

    private Block calculateLife() {
        int neighborsCount =
                topLeft.topLeft.population + topLeft.topRight.population + topRight.topLeft.population +
                        topLeft.bottomLeft.population + topRight.bottomLeft.population +
                        bottomLeft.topLeft.population + bottomLeft.topRight.population + bottomRight.topLeft.population;
        Block _tl = calculateLife(neighborsCount, topLeft.bottomRight);

        neighborsCount =
                topLeft.topRight.population + topRight.topLeft.population + topRight.topRight.population +
                        topLeft.bottomRight.population + topRight.bottomRight.population +
                        bottomLeft.topRight.population + bottomRight.topLeft.population + bottomRight.topRight.population;
        Block _tr = calculateLife(neighborsCount, topRight.bottomLeft);

        neighborsCount =
                topLeft.bottomLeft.population + topLeft.bottomRight.population + topRight.bottomLeft.population +
                        bottomLeft.topLeft.population + bottomRight.topLeft.population +
                        bottomLeft.bottomLeft.population + bottomLeft.bottomRight.population + bottomRight.bottomLeft.population;
        Block _bl = calculateLife(neighborsCount, bottomLeft.topRight);

        neighborsCount =
                topLeft.bottomRight.population + topRight.bottomLeft.population + topRight.bottomRight.population +
                        bottomLeft.topRight.population + bottomRight.topRight.population +
                        bottomLeft.bottomRight.population + bottomRight.bottomLeft.population + bottomRight.bottomRight.population;
        Block _br = calculateLife(neighborsCount, bottomRight.topLeft);


        return createBlock(1, _tl, _tr, _bl, _br);
    }

    private Block calculateLife(int neighborsCount, Block current) {
        Block block;
        if (neighborsCount == 3) {
            block = createBlock(true);
        } else if (neighborsCount == 2) {
            block = createBlock(current.alive);
        } else {
            block = createBlock(false);
        }
        return block;
    }

    public Block setLifeAt(int x, int y, boolean isAlive) {

        if (level == 1) {
            if (x < 0) {
                if (y < 0)
                    return createBlock(level, topLeft, topRight, createBlock(isAlive), bottomRight);
                else
                    return createBlock(level, createBlock(isAlive), topRight, bottomLeft, bottomRight);
            } else {
                if (y < 0)
                    return createBlock(level, topLeft, topRight, bottomLeft, createBlock(isAlive));
                else
                    return createBlock(level, topLeft, createBlock(isAlive), bottomLeft, bottomRight);
            }
        } else {
            int offset = 1 << (level - 2);
            if (x < 0) {
                if (y < 0)
                    return createBlock(level, topLeft, topRight, bottomLeft.setLifeAt(x + offset, y + offset, isAlive), bottomRight);
                else
                    return createBlock(level, topLeft.setLifeAt(x + offset, y - offset, isAlive), topRight, bottomLeft, bottomRight);
            } else {
                if (y < 0)
                    return createBlock(level, topLeft, topRight, bottomLeft, bottomRight.setLifeAt(x - offset, y + offset, isAlive));
                else
                    return createBlock(level, topLeft, topRight.setLifeAt(x - offset, y - offset, isAlive), bottomLeft, bottomRight);
            }
        }
    }

    public Collection<Point> getAllAliveCells() {

        if (cachedPoints == null) {

            cachedPoints = new HashSet<>();

            if (level == 1) {

                if (topLeft.population == 1) cachedPoints.add(new Point(0 - 1, 0));
                if (topRight.population == 1) cachedPoints.add(new Point(0, 0));
                if (bottomLeft.population == 1) cachedPoints.add(new Point(0 - 1, 0 - 1));
                if (bottomRight.population == 1) cachedPoints.add(new Point(0, 0 - 1));

            } else {

                int offset = 1 << (level - 2);

                cachedPoints.addAll(transform(topLeft.getAllAliveCells(), -offset, +offset));
                cachedPoints.addAll(transform(topRight.getAllAliveCells(), +offset, +offset));
                cachedPoints.addAll(transform(bottomLeft.getAllAliveCells(), -offset, -offset));
                cachedPoints.addAll(transform(bottomRight.getAllAliveCells(), +offset, -offset));
            }
        }

        return cachedPoints;
    }

    private Collection<? extends Point> transform(Collection<Point> cells, int xOffset, int yOffset) {
        Point offset = new Point(xOffset, yOffset);
        return cells.stream()
                .map(point -> point.add(offset))
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Block expand() {
        return createBlock(level + 1,
                createBlock(level, createBlock(level - 1), createBlock(level - 1), createBlock(level - 1), topLeft),
                createBlock(level, createBlock(level - 1), createBlock(level - 1), topRight, createBlock(level - 1)),
                createBlock(level, createBlock(level - 1), bottomLeft, createBlock(level - 1), createBlock(level - 1)),
                createBlock(level, bottomRight, createBlock(level - 1), createBlock(level - 1), createBlock(level - 1))
        );
    }

    public Block intern() {
        Block intern = internCache.get(this);
        if (intern == null) {
            intern = this;
            internCache.put(this, this);
        }
        return intern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;

        Block block = (Block) o;

        if (level != block.level) return false;

        if (level == 0) return population == block.population;

        if (bottomLeft != block.bottomLeft) return false;
        if (bottomRight != block.bottomRight) return false;
        if (topLeft != block.topLeft) return false;
        if (topRight != block.topRight) return false;

        return true;
    }

    @Override
    public int hashCode() {
        if (level == 0) return population;
        return System.identityHashCode(topRight) +
                11 * System.identityHashCode(topLeft) +
                101 * System.identityHashCode(bottomRight) +
                1007 * System.identityHashCode(bottomLeft);
    }

    @Override
    public String toString() {
        return "{" +
                "p=" + population +
                '}';
    }

    public static void cleanInternalCache() {
        internCache.clear();
    }

    public static int getInternalCacheSize() {
        return internCache.size();
    }

    public int getPopulation() {
        return population;
    }
}
