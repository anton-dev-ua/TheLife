package thelife;

public class FpsCalculator {
    private long startTime;
    private long frames;
    private double fps;

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
            fps = ((double) frames / (double) totalSpentTime) * 1000.0;
        }

        if (totalSpentTime > 5 * 1000) {
            frames = 0;
            startTime = System.currentTimeMillis();
        }
    }

    public double getFps() {
        return fps;
    }
}