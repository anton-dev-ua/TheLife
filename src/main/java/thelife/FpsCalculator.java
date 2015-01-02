package thelife;

public class FpsCalculator {
    long startTime;
    long frames;

    public FpsCalculator() {
    }

    void beforeProcess() {
        startTime = System.currentTimeMillis();
        frames = 0;
    }

    void calculateFrames() {
        frames++;

        long totalSpentTime = System.currentTimeMillis() - startTime;
        if (frames % 25 == 0 && totalSpentTime > 0) {
            System.out.printf("FPS: %5.2f\n", (((double) frames / (double) totalSpentTime) * 1000));
        }

        if (totalSpentTime > 5 * 1000) {
            frames = 0;
            startTime = System.currentTimeMillis();
        }
    }
}