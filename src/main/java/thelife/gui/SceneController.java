package thelife.gui;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.StringUtils;
import thelife.engine.LifParser;
import thelife.engine.Point;
import thelife.engine.Universe;

import java.util.Collection;

public class SceneController {


    private Universe universe;
    private SceneVisualizer sceneVisualizer;
    private final SceneScreenConverter sceneScreen;
    private double startX;
    private double startY;
    private boolean removeLife;
    private Point pointToChange;


    public SceneController(Universe universe) {
        this.universe = universe;
        sceneScreen = new SceneScreenConverter(900, 600, 16);
        sceneVisualizer = new SceneVisualizer(universe, sceneScreen);
    }

    public Group getScenePane() {
        Group pane = sceneVisualizer.buildScenePane();

        pane.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.isSecondaryButtonDown()) {
                startX = event.getX();
                startY = event.getY();
            }

            if (event.isPrimaryButtonDown()) {
                double sx = event.getX();
                double sy = event.getY();
                int x = sceneScreen.toUniverseX(sx);
                int y = sceneScreen.toUniverseY(sy);

                pointToChange = new Point(x, y);
                if (universe.getAllAliveCells().contains(pointToChange)) {
                    universe.removeLife(pointToChange);
                    removeLife = true;
                } else {
                    universe.addLife(pointToChange);
                    removeLife = false;
                }

                sceneVisualizer.displayLife();
            }

        });

        pane.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.isSecondaryButtonDown()) {
                double dx = -((event.getX() - startX) / sceneScreen.getScale());
                double dy = ((event.getY() - startY) / sceneScreen.getScale());
                startX = event.getX();
                startY = event.getY();
                sceneScreen.moveSceneCenterX(dx);
                sceneScreen.moveSceneCenterY(dy);
                sceneScreen.rescale();
                sceneVisualizer.redrawScene();
            }

            if (event.isPrimaryButtonDown()) {
                double sx = event.getX();
                double sy = event.getY();
                int x = sceneScreen.toUniverseX(sx);
                int y = sceneScreen.toUniverseY(sy);

                Point point = new Point(x, y);
                if (!pointToChange.equals(point)) {
                    pointToChange = point;
                    if (removeLife) {
                        universe.removeLife(pointToChange);
                    } else {
                        universe.addLife(pointToChange);
                    }
                    sceneVisualizer.displayLife();
                }
            }
        });


        return pane;
    }

    void redrawScene() {
        sceneVisualizer.redrawScene();
    }

    void zoomIn() {
        sceneVisualizer.zoomIn();
    }

    void zoomOut() {
        sceneVisualizer.zoomOut();
    }

    void changeHeightFor(Number oldValue, Number newValue) {
        sceneVisualizer.changeHeightFor(newValue.doubleValue() - oldValue.doubleValue());
    }

    void changeWidthFor(Number oldValue, Number newValue) {
        sceneVisualizer.changeWidthFor(newValue.doubleValue() - oldValue.doubleValue());
    }

    void parseAndSetState(String content) {
        if (isLifFormat(content)) {
            String lifString = content.toUpperCase().replaceAll("O|o", "*");
            Collection<Point> pointList = new LifParser().parse(lifString);
            universe.setState(pointList);
            redrawScene();
        }
    }

    private boolean isLifFormat(String content) {
        return StringUtils.containsOnly(content.replaceAll("\\s", ""), '.', '*', 'O', 'o');
    }
}
