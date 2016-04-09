package thelife.gui;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import thelife.engine.Point;
import thelife.engine.Universe;

import java.util.Collection;

public class SceneVisualizer {
    private static final Color gridColor = Color.LIGHTGRAY;
    private static final Color cellColor = Color.BLACK;
    private static final Color gridColor10 = Color.DARKGRAY;

    private SceneScreenConverter sceneScreen;
    private Group sceneCells;
    private Group gridLines;
    private Universe universe;
    private Group scene;
    private Group sceneNumbers;
    private Group controlButtons;

    public SceneVisualizer(Universe universe, SceneScreenConverter sceneScreen) {
        this.universe = universe;
        this.sceneScreen = sceneScreen;
    }

    public Group buildScenePane() {
        sceneCells = new Group();
        gridLines = new Group();
        sceneNumbers = new Group();
        controlButtons = new Group();

        scene = new Group();
        scene.getChildren().addAll(sceneCells, gridLines, sceneNumbers, controlButtons);

        Button button = new Button("");
        button.getStylesheets().add(getClass().getResource("/button.css").toExternalForm());
        button.setLayoutX(5);
        button.setLayoutY(5);
        button.setOnAction(event -> {
            sceneScreen.setSceneCenterX(0);
            sceneScreen.setSceneCenterY(0);
            sceneScreen.rescale();
            redrawScene();
        });
        controlButtons.getChildren().add(button);


        return scene;
    }


    public void drawSceneGrid() {

//        Rectangle clip = new Rectangle(sceneScreen.getSceneWidth(), sceneScreen.getSceneHeight());
//        scene.setClip(clip);

        gridLines.getChildren().clear();

        sceneScreen.fieldColumns().filter(this::shouldShowGridLine).forEach(this::addHorizontalGridLine);
        sceneScreen.fieldRows().filter(this::shouldShowGridLine).forEach(this::addVerticalGridLine);

        Rectangle rectangle = new Rectangle(0.5, 0.5, sceneScreen.getSceneWidth() - 1, sceneScreen.getSceneHeight() - 1);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);

        sceneNumbers.getChildren().clear();

        addSceneNumber("left", "" + (int) sceneScreen.getSceneLeft());
        addSceneNumber("right", "" + (int) sceneScreen.getSceneRight());
        addSceneNumber("top", "" + (int) sceneScreen.getSceneTop());
        addSceneNumber("bottom", "" + (int) sceneScreen.getSceneBottom());

        gridLines.getChildren().add(rectangle);
    }

    private void addSceneNumber(String position, String text) {
        Text left = new Text(text);
        Bounds boundRect = left.getLayoutBounds();
        double x = 0, y = 0;
        switch (position) {
            case "left":
                x = 5;
                y = sceneScreen.getSceneHeight() / 2 + boundRect.getHeight();
                break;
            case "right":
                x = sceneScreen.getSceneWidth() - boundRect.getWidth() - 5;
                y = sceneScreen.getSceneHeight() / 2 + boundRect.getHeight();
                break;
            case "top":
                x = sceneScreen.getSceneWidth()/2 - boundRect.getWidth() / 2;
                y = 5 + boundRect.getHeight();
                break;
            case "bottom":
                x = sceneScreen.getSceneWidth()/2 - boundRect.getWidth() / 2;
                y = sceneScreen.getSceneHeight() - 5;

        }
        left.setX(x);
        left.setY(y);
        left.setStroke(Color.BLACK);
        left.setStrokeWidth(0.5);
        left.setFill(Color.WHITE);
        sceneNumbers.getChildren().add(left);
    }

    private boolean shouldShowGridLine(int index) {
        return sceneScreen.getScale() >= 5 || index % 10 == 0 && sceneScreen.getScale() >= 2;
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
        Collection<? extends Point> allAliveCells = universe.getAllAliveCells();
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

    private void changeScale(double targetScale) {

        new Thread(() -> {
            double startScale = sceneScreen.getScale();
            int stepCount = 10;
            double dScale = (targetScale - startScale) / stepCount;
            for (int step = 0; step < stepCount; step++) {
                final double newScale = startScale + step * dScale;
                sceneScreen.changeScale(newScale);
                Platform.runLater(() -> redrawScene());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            sceneScreen.changeScale(targetScale);
            Platform.runLater(() -> redrawScene());
        }).start();

    }

    public void setUniverse(Universe universe) {
        this.universe = universe;
    }
}