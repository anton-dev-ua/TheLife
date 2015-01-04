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
    private double sceneWidth = 900;
    private double sceneHeight = 600;
    private double sceneCellSize = 9;

    private double sceneCenterX = 0;
    private double sceneCenterY = 0;

    private double sceneColumns = sceneWidth / sceneCellSize;
    private double sceneRows = sceneHeight / sceneCellSize;
    private double sceneBottom = sceneCenterY - sceneRows / 2;
    private double sceneLeft = sceneCenterX - sceneColumns / 2;
    private double sceneTop = sceneCenterY + sceneRows / 2;
    private double sceneRight = sceneCenterX + sceneColumns / 2 - 1;


    private Group sceneCells;
    private World world;
    private Space space;
    private Group lines;
    private boolean simulate;
    private double fieldOffsetY;
    private double fieldOffsetX;

    private void changeScale(double newCellSize) {
        sceneCellSize = newCellSize;

        sceneColumns = sceneWidth / sceneCellSize;
        sceneRows = sceneHeight / sceneCellSize;
        sceneBottom = sceneCenterY - sceneRows / 2;
        sceneLeft = sceneCenterX - sceneColumns / 2;
        sceneTop = sceneCenterY + sceneRows / 2;
        sceneRight = sceneCenterX + sceneColumns / 2;

        fieldOffsetY = sceneBottom * sceneCellSize + sceneHeight;
        fieldOffsetX = -sceneLeft * sceneCellSize;

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
        changeScale(35);
        displayLife();

        primaryStage.setScene(scene);
//        primaryStage.sizeToScene();
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> simulate = false);

    }

    private GridPane initTopGroup() {
        GridPane topGroup = new GridPane();
        topGroup.setHgap(10);

        addButton(topGroup, "Reset", event -> reset());
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

    private void reset() {
        initWorld();
        displayLife();
    }

    private void drawGrid() {
        lines.getChildren().clear();

        for (int y = (int) sceneBottom; y <= sceneTop; y++) {
            addGridLine(0, fieldOffsetY - y * sceneCellSize, sceneColumns * sceneCellSize, fieldOffsetY - y * sceneCellSize);
        }

        for (int x = (int) sceneLeft; x <= sceneRight; x++) {
            addGridLine(fieldOffsetX + x * sceneCellSize, 0, fieldOffsetX + x * sceneCellSize, sceneRows * sceneCellSize);
        }

        Rectangle rectangle = new Rectangle(0, 0, sceneWidth, sceneHeight);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);

        lines.getChildren().add(rectangle);
    }

    private void addGridLine(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(gridColor);
        line.setStrokeWidth(0.5);
        lines.getChildren().add(line);
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
                .filter(p -> p.getX() >= Math.floor(sceneLeft) && p.getX() < sceneRight && p.getY() > sceneBottom && p.getY() <= Math.ceil(sceneTop))
                .forEach(point -> placeCell(point.getX(), point.getY()));
    }

    private void placeCell(int x, int y) {

        Shape cell;
        double sceneX = fieldOffsetX + x * sceneCellSize + 0.5;
        double sceneY = fieldOffsetY - y * sceneCellSize + 0.5;
        double width = sceneCellSize - 1;
        double height = sceneCellSize - 1;

        if (sceneX < 0) {
            width -= -sceneX;
            sceneX = 0;
        }

        if (sceneY < 0) {
            height -= -sceneY;
            sceneY = 0;
        }

        if (sceneX + width > sceneWidth) {
            width -= sceneX + width - sceneWidth;
        }

        if (sceneY + height > sceneHeight) {
            height -= sceneY + height - sceneHeight;
        }

        cell = new Rectangle(sceneX, sceneY, width, height);

        cell.setFill(cellColor);
        sceneCells.getChildren().add(cell);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
