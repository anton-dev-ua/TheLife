package thelife.engine.hashlife;

public class HashLifeBlock extends Block {

    private HashLifeBlock(int aLevel) {
        super(aLevel);
    }

    private HashLifeBlock(int aLevel, Block topLeft, Block topRight, Block bottomLeft, Block bottomRight) {
        super(aLevel, topLeft, topRight, bottomLeft, bottomRight);
    }

    private HashLifeBlock(boolean alive) {
        super(alive);
    }

    @Override
    Block createBlock(int aLevel) {
        return new HashLifeBlock(aLevel).intern();
    }

    @Override
    Block createBlock(int aLevel, Block topLeft, Block topRight, Block bottomLeft, Block bottomRight) {
        return new HashLifeBlock(aLevel, topLeft, topRight, bottomLeft, bottomRight).intern();
    }

    @Override
    Block createBlock(boolean alive) {
        return new HashLifeBlock(alive).intern();
    }

    static Block create(int level) {
        return new HashLifeBlock(level).intern();
    }

    @Override
    protected Block centerSubBlock() {
        return createBlock(level - 1, topLeft.bottomRight, topRight.bottomLeft, bottomLeft.topRight, bottomRight.topLeft).nextGen();
    }

    @Override
    protected Block centerBlock() {
        return nextGen();
    }

    @Override
    protected Block vertical(Block top, Block bottom) {
        return createBlock(level - 1, top.bottomLeft, top.bottomRight, bottom.topLeft, bottom.topRight).nextGen();
    }

    @Override
    protected Block horizontal(Block left, Block right) {
        return createBlock(level - 1, left.topRight, right.topLeft, left.bottomRight, right.bottomLeft).nextGen();
    }
}
