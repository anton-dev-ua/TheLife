package thelife.gui;

public class Delayer {
    private long delayTime;
    private long iterationStartTime;
    private long startTime;
    private long iterations;

    public Delayer() {
        this(100);
    }

    public Delayer(long delayTime) {
        this.delayTime = delayTime;
        beforeProcess();
    }

    void beforeProcess() {
        iterations = 0;
        startTime = System.currentTimeMillis();
        iterationStartTime = startTime;
    }

    public void delayIteration() {
        if (delayTime > 0) {
            try {
                long iterationFinishTime = System.currentTimeMillis();
                long delayError = (iterationFinishTime - startTime) - delayTime * iterations;
                long waitTime = delayTime - (iterationFinishTime - iterationStartTime) - delayError;
                if (waitTime > 0) {
                    Thread.sleep(waitTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        iterationStartTime = System.currentTimeMillis();
        iterations++;
    }
}