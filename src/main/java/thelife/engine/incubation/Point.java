package thelife.engine.incubation;

public class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;

        Point anotherPoint = (Point) o;

        if (x != anotherPoint.x) return false;
        if (y != anotherPoint.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = (result << 6) + y;
        return result;
    }

    @Override
    public String toString() {
        return "{" + "x=" + x + ", y=" + y + '}';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point add(Point another) {
        return new Point(x + another.x, y + another.y);
    }
}
