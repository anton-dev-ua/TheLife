package thelife.engine.incubation;

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

        space.getTiles().forEach((point, lifeCounter) -> {
            if (lifeCounter.getLifeCount() == 3) {
                if (space.noLifeAt(point)) {
                    toBorn.add(point);
                }
            } else if (lifeCounter.getLifeCount() != 4) {
                if (space.isLifeAt(point)) {
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
