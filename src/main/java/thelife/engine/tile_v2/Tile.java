package thelife.engine.tile_v2;

import thelife.engine.Point;

public class Tile {
    private int lifeCount = 0;
    private boolean lifeInCenter;
    private int[] changedInGeneration = new int[2];

    private Point point;

    public Tile(Point point) {
        this.point = point;
    }

    public void inc() {
        lifeCount++;
    }

    public void dec() {
        lifeCount--;
    }

    public int getLifeCount() {
        return lifeCount;
    }

    @Override
    public String toString() {
        return "{" +
                "l=" + lifeCount +
                ", c=" + lifeInCenter +
                '}';
    }

    public void setLifeInCenter(boolean lifeInCenter) {
        this.lifeInCenter = lifeInCenter;
    }

    public boolean isLifeInCenter() {
        return lifeInCenter;
    }

    public boolean noLifeInCenter() {
        return !lifeInCenter;
    }

    public Point getPoint() {
        return point;
    }

    public int getChangedInGeneration(int evenOrOdd) {
        return changedInGeneration[evenOrOdd];
    }

    public void setChangedInGeneration(int evenOrOdd, int changedInGeneration) {
        this.changedInGeneration[evenOrOdd] = changedInGeneration;
    }
}
