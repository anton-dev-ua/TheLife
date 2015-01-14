package thelife.engine.hashlife;


import java.math.BigInteger;

public class Universe extends CachedQuadTreeUniverse {

    @Override
    protected Block createInitialBlock() {
        return HashLifeBlock.create(3);
    }

    @Override
    protected BigInteger incGeneration(BigInteger generation, int level) {
        return generation.add(BigInteger.valueOf(2).pow(level - 2));
    }
}
