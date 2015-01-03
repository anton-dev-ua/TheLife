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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class Main extends Application {

    private static final Color gridColor = Color.GRAY;
    private static final Color background = Color.WHITE;
    private static final Color cellColor = Color.BLACK;
    private int sceneWidth = 900;
    private int sceneHeight = 700;
    private int sceneCellSize = 9;

    private int sceneCenterX = 0;
    private int sceneCenterY = 0;

    private int sceneColumns = sceneWidth / sceneCellSize;
    private int sceneRows = sceneHeight / sceneCellSize;
    private int sceneBottom = sceneCenterY - sceneRows / 2 + 1;
    private int sceneLeft = sceneCenterX - sceneColumns / 2;
    private int sceneTop = sceneCenterY + sceneRows / 2;
    private int sceneRight = sceneCenterX + sceneColumns / 2 - 1;


    private Group sceneCells;
    private World world;
    private Space space;
    private Group lines;
    private boolean simulate;

    private void changeScale(int newCellSize) {
        sceneCellSize = newCellSize;

        sceneColumns = sceneWidth / sceneCellSize;
        sceneRows = sceneHeight / sceneCellSize;
        sceneBottom = sceneCenterY - sceneRows / 2 + 1;
        sceneLeft = sceneCenterX - sceneColumns / 2;
        sceneTop = sceneCenterY + sceneRows / 2;
        sceneRight = sceneCenterX + sceneColumns / 2 - 1;

        drawGrid();
        displayLife();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        GridPane topGroup = initTopGroup();

        grid.add(topGroup, 0, 0);

        primaryStage.setTitle("The Life");
        Scene scene = new Scene(grid, background);

        initKeyEvenets(scene);

        Group field = new Group();

        lines = new Group();
        field.getChildren().add(lines);
        drawGrid();

        sceneCells = new Group();
        field.getChildren().add(sceneCells);

        grid.add(field, 0, 1, 2, 1);
        grid.add(new Label("Generation:"), 0, 2);
        grid.add(new TextField("0"), 1, 2);

        initWorld();

        displayLife();

        primaryStage.setScene(scene);
//        primaryStage.sizeToScene();
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> simulate = false);

    }

    private GridPane initTopGroup() {
        GridPane topGroup = new GridPane();
        topGroup.setHgap(10);

        addButton(topGroup, "Run", event -> run());
        addButton(topGroup, "Stop", event -> simulate = false);
        addButton(topGroup, "+", event -> changeScale(sceneCellSize + 1));
        addButton(topGroup, "-", event -> changeScale(sceneCellSize - 1));

        return topGroup;
    }

    private void addButton(GridPane topGroup, String name, EventHandler<ActionEvent> action) {
        Button runButton = new Button(name);
        runButton.setOnAction(action);
        topGroup.add(runButton, topGroup.getChildren().size(), 0);
    }

    private void initKeyEvenets(Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if (event.getCode() == KeyCode.T) {
                    run();
                }
            }
        });
    }

    private void run() {
        new Thread(() -> {
            FpsCalculator fpsCalculator = new FpsCalculator();
            fpsCalculator.beforeProcess();

            Delayer delayer = new Delayer(50);
            delayer.beforeProcess();
            simulate = true;
            while (simulate) {
                world.nextGeneration();

                redraw();

                delayer.delayIteration();
                fpsCalculator.calculateFrames();
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

    private Group drawGrid() {
        lines.getChildren().clear();
        for (int i = 0; i <= sceneRows; i++) {
            Line line = new Line(0, i * sceneCellSize, sceneColumns * sceneCellSize, i * sceneCellSize);
            line.setStroke(gridColor);
            line.setStrokeWidth(0.5);
            lines.getChildren().add(line);
        }
        for (int i = 0; i <= sceneColumns; i++) {
            Line line = new Line(i * sceneCellSize, 0, i * sceneCellSize, sceneRows * sceneCellSize);
            line.setStroke(gridColor);
            line.setStrokeWidth(0.5);
            lines.getChildren().add(line);
        }
        return lines;
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
        allAliveCells.stream()
                .filter(p -> p.getX() >= sceneLeft && p.getX() <= sceneRight && p.getY() >= sceneBottom && p.getY() <= sceneTop)
                .forEach(point -> placeCell(point.getX(), point.getY()));
    }

    private void placeCell(int x, int y) {
        Shape cell;
        cell = new Rectangle(
                -sceneLeft * sceneCellSize + x * sceneCellSize + 0.5,
                (sceneBottom - 1) * sceneCellSize + (sceneRows * sceneCellSize) - y * sceneCellSize + 0.5,
                sceneCellSize - 1, sceneCellSize - 1
        );
        cell.setFill(cellColor);
        sceneCells.getChildren().add(cell);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
