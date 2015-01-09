package thelife.engine;

import java.util.Collection;

public interface Universe {
    void nextGeneration();

    int getGeneration();

    void clear();

    Collection<Point> getAllAliveCells();

    void setState(Collection<Point> points);
}
