package thelife;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import thelife.engine.Space;
import thelife.engine.Universe;

import java.util.concurrent.CountDownLatch;

public class Main extends Application {

    private static final Color background = Color.WHITE;

    private Universe universe;
    private Space space;
    private boolean simulate;
    private Label generationText;
    private Label populationText;
    private Label fpsText;
    private Label iterationTimeText;
    private int iterationDelayIndex = 5;
    private long iterationDelays[] = {2000, 1000, 500, 250, 100, 50, 10, 0};
    private Delayer delayer;
    private SceneVisualizer sceneVisualizer;

    @Override
    public void start(Stage primaryStage) throws Exception {

        initWorld();

        sceneVisualizer = new SceneVisualizer(space);

        primaryStage.setTitle("The Life");

        GridPane mainGrid = createMainGrid();

        mainGrid.add(buildTopGroup(), 0, 0);
        mainGrid.add(sceneVisualizer.buildScenePane(), 0, 1);
        mainGrid.add(buildStatusBar(), 0, 2);


        displayIterationDelay(5);
        sceneVisualizer.redrawScene();
        displayStatistics();

        primaryStage.setScene(new Scene(mainGrid, background));

        primaryStage.show();

        handleStageEvents(primaryStage);


    }

    private GridPane buildTopGroup() {
        GridPane topGroup = new GridPane();
        topGroup.setHgap(10);

        addButton(topGroup, "Reset", event -> reset());

        addSeparator(topGroup);

        addButton(topGroup, "Run", event -> run());
        addButton(topGroup, "Stop", event -> simulate = false);

        addSeparator(topGroup);

        addButton(topGroup, "Zoom in", event -> sceneVisualizer.zoomIn());
        addButton(topGroup, "Zoom out", event -> sceneVisualizer.zoomOut());

        addSeparator(topGroup);

        addButton(topGroup, "Speed +", event -> displayIterationDelay(iterationDelayIndex + 1));
        addButton(topGroup, "Speed -", event -> displayIterationDelay(iterationDelayIndex - 1));

        return topGroup;
    }

    private void addButton(GridPane topGroup, String name, EventHandler<ActionEvent> action) {
        Button runButton = new Button(name);
        runButton.setOnAction(action);
        topGroup.add(runButton, topGroup.getChildren().size(), 0);
    }

    private void addSeparator(GridPane topGroup) {
        topGroup.add(new Separator(), topGroup.getChildren().size(), 0);
    }

    private GridPane buildStatusBar() {
        GridPane bottomGrid = new GridPane();
        bottomGrid.setHgap(5);
        generationText = addStatusInfoLabel(bottomGrid, "Generation:");
        populationText = addStatusInfoLabel(bottomGrid, "Population:");
        fpsText = addStatusInfoLabel(bottomGrid, "FPS");
        iterationTimeText = addStatusInfoLabel(bottomGrid, "Iteration time:");
        return bottomGrid;
    }

    private Label addStatusInfoLabel(GridPane bottomGrid, String name) {
        Label infoText = new Label("0");
        bottomGrid.add(new Label(name), bottomGrid.getChildren().size(), 0);
        infoText.setMinWidth(40);
        bottomGrid.add(infoText, bottomGrid.getChildren().size(), 0);
        return infoText;
    }

    private void handleStageEvents(Stage primaryStage) {
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) ->
                sceneVisualizer.changeWidthFor(newValue.doubleValue() - oldValue.doubleValue()));

        primaryStage.heightProperty().addListener((observable, oldValue, newValue) ->
                sceneVisualizer.changeHeightFor(newValue.doubleValue() - oldValue.doubleValue()));

        primaryStage.setOnCloseRequest(event -> simulate = false);
    }

    private GridPane createMainGrid() {
        GridPane mainGrid = new GridPane();
        mainGrid.setVgap(10);
        mainGrid.setHgap(10);
        mainGrid.setPadding(new Insets(10, 10, 10, 10));
        return mainGrid;
    }

    private void displayIterationDelay(int newDelay) {
        if (newDelay >= 0 && newDelay < iterationDelays.length) {
            iterationDelayIndex = newDelay;
            iterationTimeText.setText(String.format("%2.2fs", (double) iterationDelays[iterationDelayIndex] / 1000.0));
            if (delayer != null) {
                delayer = new Delayer(iterationDelays[iterationDelayIndex]);
            }
        }
    }

    private void run() {
        new Thread(() -> {
            FpsCalculator fpsCalculator = new FpsCalculator();
            fpsCalculator.beforeProcess();

            delayer = new Delayer(iterationDelays[iterationDelayIndex]);

            simulate = true;
            while (simulate) {
                universe.nextGeneration();

                redraw();

                Platform.runLater(() -> displayStatistics());

                delayer.delayIteration();

                fpsCalculator.calculateFrames();
                Platform.runLater(() -> fpsText.setText(String.format("%5.2f", fpsCalculator.getFps())));
            }

        }).start();
    }

    private void displayStatistics() {
        generationText.setText(String.valueOf(universe.getGeneration()));
        populationText.setText(String.valueOf(space.getAllAliveCells().size()));
    }

    private void initWorld() {
        space = new Space();
        universe = new Universe(space);

        initialLife();

    }

    private void initialLife() {
        universe.clear();
        space.setLifeAt(0, 2);
        space.setLifeAt(1, 2);
        space.setLifeAt(0, 1);
        space.setLifeAt(-1, 1);
        space.setLifeAt(0, 0);
    }

    private void reset() {
        initialLife();
        sceneVisualizer.displayLife();
    }

    private void redraw() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            sceneVisualizer.displayLife();
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}