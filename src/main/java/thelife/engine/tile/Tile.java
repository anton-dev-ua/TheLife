package thelife.engine.tile;

public class Tile {
    private int lifeCount = 0;
    private boolean lifeInCenter;

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
}
