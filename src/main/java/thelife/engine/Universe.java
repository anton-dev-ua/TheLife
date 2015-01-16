package thelife.engine;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigInteger;
import java.util.Collection;

public interface Universe {
    void nextGeneration();

    BigInteger getGeneration();

    void clear();

    Collection<Point> getAllAliveCells();

    void setState(Collection<Point> points);

    default void addLife(Point point) {
        throw new NotImplementedException();
    }
}
