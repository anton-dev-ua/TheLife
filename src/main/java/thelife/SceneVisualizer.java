package thelife;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import thelife.engine.Point;
import thelife.engine.Space;

import java.util.Collection;

public class SceneVisualizer {
    private static final Color gridColor = Color.LIGHTGRAY;
    private static final Color cellColor = Color.BLACK;
    private static final Color gridColor10 = Color.DARKGRAY;

    private SceneScreenConverter sceneScreen;
    private Group sceneCells;
    private Group gridLines;
    private Space space;

    public SceneVisualizer(Space space) {
        this.space = space;
        sceneScreen = new SceneScreenConverter(900, 600, 10);
    }

    public Group buildScenePane() {
        sceneCells = new Group();
        gridLines = new Group();

        Group scene = new Group();
        scene.getChildren().addAll(sceneCells, gridLines);

        return scene;
    }


    public void drawSceneGrid() {
        gridLines.getChildren().clear();

        sceneScreen.fieldColumns().filter(this::shouldShowGridLine).forEach(this::addHorizontalGridLine);
        sceneScreen.fieldRows().filter(this::shouldShowGridLine).forEach(this::addVerticalGridLine);

        Rectangle rectangle = new Rectangle(0, 0, sceneScreen.getSceneWidth(), sceneScreen.getSceneHeight());
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

    public void displayLife() {
        Collection<Point> allAliveCells = space.getAllAliveCells();
        sceneCells.getChildren().clear();

        allAliveCells.stream().filter(sceneScreen::isVisible).map(sceneScreen::toScreenRect).forEach(this::drawCell);

    }

    void drawCell(ScreenRectangle screenRect) {
        Shape cell = new Rectangle(screenRect.getX(), screenRect.getY(), screenRect.getWidth(), screenRect.getHeight());
        cell.setFill(cellColor);
        sceneCells.getChildren().add(cell);
    }

    public void redrawScene() {
        drawSceneGrid();
        displayLife();
    }

    public void changeHeightFor(double deltaY) {
        sceneScreen.changeHeightFor(deltaY);
        redrawScene();
    }

    public void changeWidthFor(double deltaX) {
        sceneScreen.changeWidthFor(deltaX);
        redrawScene();
    }

    public void zoomIn() {
       changeScale(sceneScreen.getScale() * 2);
    }

    public void zoomOut() {
        changeScale(sceneScreen.getScale() / 2);
    }

    private void changeScale(double scale) {
        sceneScreen.changeScale(scale);
        redrawScene();
    }
}