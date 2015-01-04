package thelife;

import java.util.stream.IntStream;

public class SceneScreenConverter {

    double sceneWidth = 900;
    double sceneHeight = 600;
    double sceneCellSize = 9;
    double sceneCenterX = 0;
    double sceneCenterY = 0;
    double sceneColumns;
    double sceneRows;
    double sceneBottom;
    double sceneLeft;
    double sceneTop;
    double sceneRight;
    double fieldOffsetY;
    double fieldOffsetX;

    public SceneScreenConverter() {
    }

    void rescale() {
        sceneColumns = sceneWidth / sceneCellSize;
        sceneRows = sceneHeight / sceneCellSize;
        sceneBottom = sceneCenterY - sceneRows / 2;
        sceneLeft = sceneCenterX - sceneColumns / 2;
        sceneTop = sceneCenterY + sceneRows / 2;
        sceneRight = sceneCenterX + sceneColumns / 2;

        fieldOffsetY = sceneBottom * sceneCellSize + sceneHeight;
        fieldOffsetX = -sceneLeft * sceneCellSize;
    }

    void changeHeightFor(double deltaY) {
        sceneHeight += deltaY;
        rescale();
    }

    void changeWidthFor(double deltaX) {
        sceneWidth += deltaX;
        rescale();
    }

    void changeScale(double newCellSize) {
        sceneCellSize = newCellSize;
        rescale();
    }

    double getSceneWidth() {
        return sceneWidth;
    }

    double getSceneHeight() {
        return sceneHeight;
    }

    double getScale() {
        return sceneCellSize;
    }

    IntStream fieldColumns() {
        return IntStream.rangeClosed((int) sceneLeft, (int) sceneRight);
    }

    IntStream fieldRows() {
        return IntStream.rangeClosed((int) sceneBottom, (int) sceneTop);
    }

    double toScreenX(int x) {
        return fieldOffsetX + x * sceneCellSize;
    }

    double toScreenY(int y) {
        return fieldOffsetY - y * sceneCellSize;
    }

    ScreenRectangle toScreenCoord(Point point) {
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