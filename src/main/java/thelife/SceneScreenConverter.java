package thelife;

import thelife.engine.Point;

import java.util.stream.IntStream;

public class SceneScreenConverter {

    private double sceneWidth = 900;
    private double sceneHeight = 600;
    private double sceneCellSize = 9;
    private double sceneCenterX = 0;
    private double sceneCenterY = 0;
    private double sceneColumns;
    private double sceneRows;
    private double sceneBottom;
    private double sceneLeft;
    private double sceneTop;
    private double sceneRight;
    private double fieldOffsetY;
    private double fieldOffsetX;

    public SceneScreenConverter(double sceneWidth, double sceneHeight, double scale) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.sceneCellSize = scale;
        rescale();
    }

    private void rescale() {
        sceneColumns = sceneWidth / sceneCellSize;
        sceneRows = sceneHeight / sceneCellSize;
        sceneBottom = sceneCenterY - sceneRows / 2;
        sceneLeft = sceneCenterX - sceneColumns / 2;
        sceneTop = sceneCenterY + sceneRows / 2;
        sceneRight = sceneCenterX + sceneColumns / 2;

        fieldOffsetY = sceneBottom * sceneCellSize + sceneHeight;
        fieldOffsetX = -sceneLeft * sceneCellSize;
    }

    public void changeHeightFor(double deltaY) {
        sceneHeight += deltaY;
        rescale();
    }

    public void changeWidthFor(double deltaX) {
        sceneWidth += deltaX;
        rescale();
    }

    public void changeScale(double newCellSize) {
        sceneCellSize = newCellSize;
        rescale();
    }

    public double getSceneWidth() {
        return sceneWidth;
    }

    public double getSceneHeight() {
        return sceneHeight;
    }

    public double getScale() {
        return sceneCellSize;
    }

    public IntStream fieldColumns() {
        return IntStream.rangeClosed((int) sceneLeft, (int) sceneRight);
    }

    public IntStream fieldRows() {
        return IntStream.rangeClosed((int) sceneBottom, (int) sceneTop);
    }

    public double toScreenX(int x) {
        return fieldOffsetX + x * sceneCellSize;
    }

    public double toScreenY(int y) {
        return fieldOffsetY - y * sceneCellSize;
    }

    public ScreenRectangle toScreenRect(Point point) {
        double screenX = toScreenX(point.getX());
        double screenY = toScreenY(point.getY());
        double width = getScale();
        double height = getScale();

        if (screenX < 0) {
            width -= -screenX;
            screenX = 0;
        }

        if (screenY < 0) {
            height -= -screenY;
            screenY = 0;
        }

        if (screenX + width > sceneWidth) {
            width -= screenX + width - sceneWidth;
        }

        if (screenY + height > sceneHeight) {
            height -= screenY + height - sceneHeight;
        }

        return new ScreenRectangle(screenX, screenY, width, height);
    }

    boolean isVisible(Point p) {
        return p.getX() >= Math.floor(sceneLeft) && p.getX() < sceneRight && p.getY() > sceneBottom && p.getY() <= Math.ceil(sceneTop);
    }
}