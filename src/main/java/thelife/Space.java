package thelife;

import java.util.HashMap;
import java.util.Map;

public class Space {

    private Map<CellKey, Boolean> field = new HashMap<>();

    public void setLifeAt(int x, int y) {
        field.put(new CellKey(x, y), true);
    }

    public boolean isLifeAt(int x, int y) {
        return field.get(new CellKey(x, y)) == Boolean.TRUE;
    }

    public int getAliveNeighborsCountAt(int x, int y) {

        int neighborsCount = 0;
        for (int xNeighbor = -1; xNeighbor <= 1; xNeighbor++) {
            for (int yNeighbor = -1; yNeighbor <= 1; yNeighbor++) {
                if ((xNeighbor != 0 || yNeighbor != 0) && isLifeAt(x + xNeighbor, y + yNeighbor)) {
                    neighborsCount++;
                }
            }
        }
        return neighborsCount;
    }

    public void removeLifeAt(int x, int y) {
        field.remove(new CellKey(x, y));
    }

    static class CellKey {
        int x;
        int y;

        public CellKey(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CellKey)) return false;

            CellKey cellKey = (CellKey) o;

            if (x != cellKey.x) return false;
            if (y != cellKey.y) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }
}
