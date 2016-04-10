package thelife.engine;

import java.math.BigInteger;
import java.util.Collection;

public interface Universe {
    void nextGeneration();

    BigInteger getGeneration();

    void clear();

    Collection<Point> getAllAliveCells();

    void setState(Collection<Point> points);

    void addLife(Point point);

    void removeLife(Point point);
}
