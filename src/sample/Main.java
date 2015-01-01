package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.random;

public class Main extends Application {

    private static final Color gridColor = Color.GRAY;
    private static final Color background = Color.WHITE;
    private static final Color cellColor = Color.BLACK;
    private static final int sceneWidth = 1000;
    private static final int sceneHeight = 1000;
    private static final int sceneCellWidth = 10;
    private static final int sceneCellHeight = 10;
    private Group sceneCells;
    private LinkedList<Shape> cells = new LinkedList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, sceneWidth, sceneHeight, background);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.A) {
                    placeCell(random() * sceneWidth / sceneCellHeight, random() * sceneHeight / sceneCellHeight);
                }

                if (event.getCode() == KeyCode.R) {
                    if (cells.size() > 0) {
                        sceneCells.getChildren().remove(cells.getLast());
                        cells.removeLast();
                    }
                }

                if (event.getCode() == KeyCode.T) {
                    new Timer().scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                placeCell(random() * sceneWidth / sceneCellHeight, random() * sceneHeight / sceneCellHeight);
                            });


                        }
                    }, 1000, 100);
                }

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


        sceneCells = new Group();
        root.getChildren().add(sceneCells);

        primaryStage.show();

    }

    private void placeCell(double x, double y) {
        placeCell((int) x, (int) y);
    }

    private void placeCell(int x, int y) {
        System.out.printf("x=%-2s, y=%-2s\n",x,y);
        Shape cell;
//        cell = new Rectangle(x * sceneCellWidth + 0.5, y * sceneCellHeight + 0.5, sceneCellWidth - 1, sceneCellHeight - 1);
//        cell.setFill(cellColor);
        cell = new Circle(x * sceneCellWidth + sceneCellWidth/2, y * sceneCellHeight + sceneCellHeight/2, sceneCellWidth/2, cellColor);
        sceneCells.getChildren().add(cell);
        cells.add(cell);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
