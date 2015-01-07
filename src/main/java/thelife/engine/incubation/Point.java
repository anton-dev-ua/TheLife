package thelife.engine.incubation;

public class Point {
    private int x;
    private int y;
    private int hashCode;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        hashCode = x;
        hashCode = (hashCode << 6) + y;
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
        return hashCode;
    }

    @Override
    public String toString() {
        return "{" + "x=" + x + ", y=" + y + '}';
    }

    public Point add(Point another) {
        return new Point(x + another.x, y + another.y);
    }
}
