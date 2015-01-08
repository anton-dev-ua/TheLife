package thelife.engine;

import java.util.HashSet;
import java.util.Set;

public class Universe {
    private Space space;
    private int generation = 0;

    public Universe(Space space) {
        this.space = space;
    }

    public void nextGeneration() {
        Set<Point> toBorn = new HashSet<>();
        Set<Point> toDie = new HashSet<>();

        space.getTiles().forEach((point, tile) -> {
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

    public int getGeneration() {
        return generation;
    }

    public void clear() {
        generation = 0;
        space.clear();
    }
}
