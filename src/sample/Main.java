package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static java.lang.Math.random;

public class Main extends Application {

    private static final Color gridColor = Color.GRAY;
    private static final Color background = Color.WHITE;
    private static final Color cellColor = Color.BLACK;
    private static final int sceneWidth = 1000;
    private static final int sceneHeight = 1000;
    private static final int sceneCellHeight = 10;
    private static final int sceneCellWidth = 10;
    private Group cells;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 1000, 1000, background);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event);
                placeCell(random() * 20, random() * 20);
            }
        });

        primaryStage.setScene(scene);


        Group lines = new Group();
        for (int i = 0; i < sceneHeight; i++) {
            Line line = new Line(0, i * sceneCellHeight, sceneWidth, i * sceneCellHeight);
            line.setStroke(gridColor);
            line.setStrokeWidth(0.5);
            lines.getChildren().add(line);
        }
        for (int i = 0; i < sceneWidth; i++) {
            Line line = new Line(i * sceneCellWidth, 0, i * sceneCellWidth, sceneHeight);
            line.setStroke(gridColor);
            line.setStrokeWidth(0.5);
            lines.getChildren().add(line);
        }
        root.getChildren().add(lines);


        cells = new Group();
        placeCell(45, 45);
        root.getChildren().add(cells);

        primaryStage.show();
    }

    private void placeCell(double x, double y) {
        placeCell((int) x, (int) y);
    }

    private void placeCell(int x, int y) {
        Rectangle rectangle = new Rectangle(x * sceneCellWidth + 0.5, y * sceneCellHeight + 0.5, sceneCellWidth - 1, sceneCellHeight - 1);
        rectangle.setFill(cellColor);
        cells.getChildren().add(rectangle);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
