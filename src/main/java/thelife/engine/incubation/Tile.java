package thelife.engine.incubation;

public class Tile {
    private int lifeCount = 0;

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
        return "Tile{" +
                "lives=" + lifeCount +
                '}';
    }
}
