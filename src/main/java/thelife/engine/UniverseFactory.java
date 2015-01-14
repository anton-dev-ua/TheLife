package thelife.engine;

import thelife.engine.hashlife.CachedQuadTreeUniverse;

public class UniverseFactory {

    public Universe createUniverse(LifeAlgorithm lifeAlgorithm) {
        switch (lifeAlgorithm) {
            case TILE:
                return new thelife.engine.tile.Universe();
            case POINT:
                return new thelife.engine.point_centric.Universe();
            case INCUBATION:
                return new CachedQuadTreeUniverse();
            case INCUBATION_V2:
                return new thelife.engine.incubation.CachedQuadTreeUniverse();
            case TILE_V2:
                return new thelife.engine.tile_v2.Universe();
        }
        return null;
    }

}
