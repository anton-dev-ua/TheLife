package thelife;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Set;

public class SceneVisualizer {
    static final Color gridColor = Color.LIGHTGRAY;
    static final Color cellColor = Color.BLACK;
    public static final Color gridColor10 = Color.DARKGRAY;
    SceneScreenConverter sceneScreen;
    Group sceneCells;
    Group gridLines;
    private Space space;

    public SceneVisualizer(Space space) {
        this.space = space;
        sceneScreen = new SceneScreenConverter(900, 600, 10);
    }

    Group buildScenePane() {
        Group scene = new Group();

        sceneCells = new Group();
        scene.getChildren().add(sceneCells);

        gridLines = new Group();
        scene.getChildren().add(gridLines);

        return scene;
    }

    void drawSceneGrid() {
        gridLines.getChildren().clear();

        sceneScreen.fieldColumns().filter(this::shouldShowGridLine).forEach(this::addHorizontalGridLine);
        sceneScreen.fieldRows().filter(this::shouldShowGridLine).forEach(this::addVerticalGridLine);

        Rectangle rectangle = new Rectangle(0, 0, sceneScreen.sceneWidth, sceneScreen.sceneHeight);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);

        gridLines.getChildren().add(rectangle);
    }

    boolean shouldShowGridLine(int y) {
        return sceneScreen.getScale() >= 5 || y % 10 == 0;
    }

    void addHorizontalGridLine(int x) {
        addGridLine(sceneScreen.toScreenX(x), 0, sceneScreen.toScreenX(x), sceneScreen.getSceneHeight(), chooseGridLineColor(x));
    }

    void addVerticalGridLine(int y) {
        addGridLine(0, sceneScreen.toScreenY(y), sceneScreen.getSceneWidth(), sceneScreen.toScreenY(y), chooseGridLineColor(y));
    }

    void addGridLine(double startX, double startY, double endX, double endY, Color color) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(color);
        line.setStrokeWidth(0.5);
        gridLines.getChildren().add(line);
    }

    Color chooseGridLineColor(int x) {
        return x % 10 == 0 && sceneScreen.getScale() >= 5 ? gridColor10 : gridColor;
    }

    void displayLife() {
        Set<Point> allAliveCells = space.getAllAliveCells();
        sceneCells.getChildren().clear();

        allAliveCells.stream().filter(sceneScreen::isVisible).forEach(this::placeCell);

    }

    void placeCell(Point point) {
        ScreenRectangle screenRect = sceneScreen.toScreenCoord(point);
        Shape cell = new Rectangle(screenRect.getX(), screenRect.getY(), screenRect.getWidth(), screenRect.getHeight());
        cell.setFill(cellColor);
        sceneCells.getChildren().add(cell);
    }

    void redrawScene() {
        drawSceneGrid();
        displayLife();
    }

    void changeScale(double newCellSize) {
        sceneScreen.changeScale(newCellSize);
        redrawScene();
    }

    void changeHeightFor(double deltaY) {
        sceneScreen.changeHeightFor(deltaY);
        redrawScene();
    }

    void changeWidthFor(double deltaX) {
        sceneScreen.changeWidthFor(deltaX);
        redrawScene();
    }
}