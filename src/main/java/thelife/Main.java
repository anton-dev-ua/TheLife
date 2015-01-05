package thelife;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class Main extends Application {

    private static final Color gridColor = Color.LIGHTGRAY;
    private static final Color background = Color.WHITE;
    private static final Color cellColor = Color.BLACK;
    public static final Color gridColor10 = Color.DARKGRAY;

    private SceneScreenConverter sceneScreen;

    private Group sceneCells;
    private World world;
    private Space space;
    private Group gridLines;
    private boolean simulate;
    private Label generationText;
    private Label populationText;
    private Label fpsText;
    private Label iterationTimeText;
    private int iterationDelayIndex = 5;
    private long iterationDelays[] = {2000, 1000, 500, 250, 100, 50, 10, 0};
    private Delayer delayer;

    private void changeScale(double newCellSize) {
        sceneScreen.changeScale(newCellSize);
        redrawScene();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        sceneScreen = new SceneScreenConverter(900, 600, 10);

        primaryStage.setTitle("The Life");

        GridPane mainGrid = createMainGrid();

        mainGrid.add(buildTopGroup(), 0, 0);
        mainGrid.add(buildScenePane(), 0, 1);
        mainGrid.add(buildStatusBar(), 0, 2);

        initWorld();

        displayIterationDelay(5);
        redrawScene();

        primaryStage.setScene(new Scene(mainGrid, background));

        primaryStage.show();

        handleStageEvenets(primaryStage);


    }

    private void handleStageEvenets(Stage primaryStage) {
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            double deltaX = newValue.doubleValue() - oldValue.doubleValue();
            sceneScreen.changeWidthFor(deltaX);
            redrawScene();

        });

        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            double deltaY = newValue.doubleValue() - oldValue.doubleValue();
            sceneScreen.changeHeightFor(deltaY);
            redrawScene();
        });

        primaryStage.setOnCloseRequest(event -> simulate = false);
    }

    private GridPane createMainGrid() {
        GridPane mainGrid = new GridPane();
        mainGrid.setVgap(10);
        mainGrid.setHgap(10);
        mainGrid.setPadding(new Insets(10, 10, 10, 10));
        return mainGrid;
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

    private Group buildScenePane() {
        Group scene = new Group();

        sceneCells = new Group();
        scene.getChildren().add(sceneCells);

        gridLines = new Group();
        scene.getChildren().add(gridLines);

        return scene;
    }

    private Label addStatusInfoLabel(GridPane bottomGrid, String name) {
        Label infoText = new Label("0");
        bottomGrid.add(new Label(name), bottomGrid.getChildren().size(), 0);
        infoText.setMinWidth(40);
        bottomGrid.add(infoText, bottomGrid.getChildren().size(), 0);
        return infoText;
    }

    private void redrawScene() {
        drawSceneGrid();
        displayLife();
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

    private GridPane buildTopGroup() {
        GridPane topGroup = new GridPane();
        topGroup.setHgap(10);

        addButton(topGroup, "Reset", event -> reset());

        addSeparator(topGroup);

        addButton(topGroup, "Run", event -> run());
        addButton(topGroup, "Stop", event -> simulate = false);

        addSeparator(topGroup);

        addButton(topGroup, "Zoom in", event -> changeScale(sceneScreen.getScale() * 2));
        addButton(topGroup, "Zoom out", event -> changeScale(sceneScreen.getScale() / 2));

        addSeparator(topGroup);

        addButton(topGroup, "Speed +", event -> displayIterationDelay(iterationDelayIndex + 1));
        addButton(topGroup, "Speed -", event -> displayIterationDelay(iterationDelayIndex - 1));

        return topGroup;
    }

    private void addSeparator(GridPane topGroup) {
        topGroup.add(new Separator(), topGroup.getChildren().size(), 0);
    }

    private void addButton(GridPane topGroup, String name, EventHandler<ActionEvent> action) {
        Button runButton = new Button(name);
        runButton.setOnAction(action);
        topGroup.add(runButton, topGroup.getChildren().size(), 0);
    }

    private void run() {
        new Thread(() -> {
            FpsCalculator fpsCalculator = new FpsCalculator();
            fpsCalculator.beforeProcess();

            delayer = new Delayer(iterationDelays[iterationDelayIndex]);

            simulate = true;
            while (simulate) {
                world.nextGeneration();

                redraw();

                delayer.delayIteration();
                fpsCalculator.calculateFrames();
                Platform.runLater(() -> fpsText.setText(String.format("%5.2f", fpsCalculator.getFps())));
            }

        }).start();
    }

    private void initWorld() {
        space = new Space();
        space.setLifeAt(0, 2);
        space.setLifeAt(1, 2);
        space.setLifeAt(0, 1);
        space.setLifeAt(-1, 1);
        space.setLifeAt(0, 0);

        world = new World(space);
    }

    private void reset() {
        initWorld();
        displayLife();
    }

    private void drawSceneGrid() {
        gridLines.getChildren().clear();

        sceneScreen.fieldColumns().filter(this::shouldShowGridLine).forEach(this::addHorizontalGridLine);
        sceneScreen.fieldRows().filter(this::shouldShowGridLine).forEach(this::addVerticalGridLine);

        Rectangle rectangle = new Rectangle(0, 0, sceneScreen.sceneWidth, sceneScreen.sceneHeight);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);

        gridLines.getChildren().add(rectangle);
    }

    private boolean shouldShowGridLine(int y) {
        return sceneScreen.getScale() >= 5 || y % 10 == 0;
    }

    private void addHorizontalGridLine(int x) {
        addGridLine(sceneScreen.toScreenX(x), 0, sceneScreen.toScreenX(x), sceneScreen.getSceneHeight(), chooseGridLineColor(x));
    }

    private void addVerticalGridLine(int y) {
        addGridLine(0, sceneScreen.toScreenY(y), sceneScreen.getSceneWidth(), sceneScreen.toScreenY(y), chooseGridLineColor(y));
    }

    private void addGridLine(double startX, double startY, double endX, double endY, Color color) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(color);
        line.setStrokeWidth(0.5);
        gridLines.getChildren().add(line);
    }

    private Color chooseGridLineColor(int x) {
        return x % 10 == 0 && sceneScreen.getScale() >= 5 ? gridColor10 : gridColor;
    }

    private void redraw() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            displayLife();
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void displayLife() {
        Set<Point> allAliveCells = space.getAllAliveCells();
        sceneCells.getChildren().clear();

        allAliveCells.stream().filter(sceneScreen::isVisible).forEach(this::placeCell);

        generationText.setText(String.valueOf(world.getGeneration()));
        populationText.setText(String.valueOf(allAliveCells.size()));
    }

    private void placeCell(Point point) {
        ScreenRectangle screenRect = sceneScreen.toScreenCoord(point);
        Shape cell = new Rectangle(screenRect.getX(), screenRect.getY(), screenRect.getWidth(), screenRect.getHeight());
        cell.setFill(cellColor);
        sceneCells.getChildren().add(cell);
    }

    public static void main(String[] args) {
        launch(args);
    }
}