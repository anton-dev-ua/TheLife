package thelife.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import thelife.engine.LifeAlgorithm;
import thelife.engine.RleParser;
import thelife.engine.UniverseFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static thelife.engine.LifeAlgorithm.INCUBATION;
import static thelife.engine.LifeAlgorithm.TILE;

public class Main extends Application {

    private static final Color background = Color.WHITE;

    public static final String R_PENTAMINO = "Pos=-1,-1 bo$2o$b2o!";
    public static final String PUFFER_TRAIN = "Pos=0,0 3bo$4bo$o3bo$b4o4$o$b2o$2bo$2bo$bo3$3bo$4bo$o3bo$b4o!";


    private thelife.engine.Universe universe;
    private boolean simulate;
    private Label generationText;
    private Label populationText;
    private Label fpsText;
    private Label iterationTimeText;
    private int iterationDelayIndex = 5;
    private long iterationDelays[] = {2000, 1000, 500, 250, 100, 50, 10, 0};
    private Delayer delayer;
    private SceneController sceneController;
    private LifeAlgorithm lifeAlgorithm = TILE;

    @Override
    public void start(Stage primaryStage) throws Exception {

        initWorld();

        sceneController = new SceneController(universe);


        primaryStage.setTitle("The Life");

        GridPane mainGrid = createMainGrid();

        mainGrid.add(buildTopGroup(), 0, 0);

        Group pane = sceneController.getScenePane();
        mainGrid.add(pane, 0, 1);

        mainGrid.add(buildStatusBar(), 0, 2);


        displayIterationDelay(5);
        sceneController.redrawScene();
        displayStatistics();

        mainGrid.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            KeyCombination keyCombination = KeyCombination.valueOf("Shortcut+V");
            if (keyCombination.match(event)) {
                String content = Clipboard.getSystemClipboard().getString();

                if (content != null) {
                    sceneController.parseAndSetState(content);
                }

            }
        });

        primaryStage.setScene(new Scene(mainGrid, background));

        primaryStage.show();

        handleStageEvents(primaryStage);


    }

    private GridPane buildTopGroup() {
        GridPane topGroup = new GridPane();
        topGroup.setHgap(10);

        addButton(topGroup, "Reset", event -> reset());

        addSeparator(topGroup);

        addButton(topGroup, "Step", event -> {
            universe.nextGeneration();
            sceneController.redrawScene();
            displayStatistics();
        });
        addButton(topGroup, "Run", event -> run());
        addButton(topGroup, "Stop", event -> stopEmulation());

        addSeparator(topGroup);

        addButton(topGroup, "Zoom in", event -> sceneController.zoomIn());
        addButton(topGroup, "Zoom out", event -> sceneController.zoomOut());

        addSeparator(topGroup);

        addButton(topGroup, "Speed +", event -> displayIterationDelay(iterationDelayIndex + 1));
        addButton(topGroup, "Speed -", event -> displayIterationDelay(iterationDelayIndex - 1));

        addSeparator(topGroup);

        addAlgorithmsComboBox(topGroup);
        
        return topGroup;
    }

    private void addAlgorithmsComboBox(GridPane topGroup) {                                                                                       
        
        topGroup.add(new Label("Algorithm:"),topGroup.getChildren().size(), 0);
        
        Collection<LifeAlgorithm> lifeAlgorithms = Arrays.stream(LifeAlgorithm.values()).filter(it -> it != INCUBATION).collect(Collectors.toList());
        ObservableList<LifeAlgorithm> options = FXCollections.observableArrayList(lifeAlgorithms);
        ComboBox<LifeAlgorithm> comboBox = new ComboBox<>(options);
        comboBox.setValue(lifeAlgorithm);
        topGroup.add(comboBox, topGroup.getChildren().size(), 0);

        comboBox.setOnAction(event -> {
            System.out.println(comboBox.getValue());
            lifeAlgorithm = comboBox.getValue();
            initWorld();
            sceneController.setUniverse(universe);
            sceneController.redrawScene();
        });
    }

    private void stopEmulation() {
        simulate = false;
        displayStatistics();
        sceneController.redrawScene();
    }

    private void addButton(GridPane topGroup, String name, EventHandler<ActionEvent> action) {
        Button runButton = new Button(name);
        runButton.setOnAction(action);
        topGroup.add(runButton, topGroup.getChildren().size(), 0);
    }

    private void addSeparator(GridPane topGroup) {
        topGroup.add(new Separator(), topGroup.getChildren().size(), 0);
    }

    private GridPane buildStatusBar() {
        GridPane bottomGrid = new GridPane();
        bottomGrid.setHgap(5);
        generationText = addStatusInfoLabel(bottomGrid, "Generation:");
        populationText = addStatusInfoLabel(bottomGrid, "Population:");
        fpsText = addStatusInfoLabel(bottomGrid, "FPS");
        iterationTimeText = addStatusInfoLabel(bottomGrid, "Iteration time:");
        return bottomGrid;
    }

    private Label addStatusInfoLabel(GridPane bottomGrid, String name) {
        Label infoText = new Label("0");
        bottomGrid.add(new Label(name), bottomGrid.getChildren().size(), 0);
        infoText.setMinWidth(40);
        bottomGrid.add(infoText, bottomGrid.getChildren().size(), 0);
        return infoText;
    }

    private void handleStageEvents(Stage primaryStage) {
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) ->
                sceneController.changeWidthFor(oldValue, newValue));

        primaryStage.heightProperty().addListener((observable, oldValue, newValue) ->
                sceneController.changeHeightFor(oldValue, newValue));

        primaryStage.setOnCloseRequest(event -> simulate = false);
    }

    private GridPane createMainGrid() {
        GridPane mainGrid = new GridPane();
        mainGrid.setVgap(10);
        mainGrid.setHgap(10);
        mainGrid.setPadding(new Insets(10, 10, 10, 10));
        return mainGrid;
    }

    private void displayIterationDelay(int newDelay) {
        if (newDelay >= 0 && newDelay < iterationDelays.length) {
            iterationDelayIndex = newDelay;
            iterationTimeText.setText(String.format("%2.2fs", (double) iterationDelays[iterationDelayIndex] / 1000.0));
            if (delayer != null) {
                delayer = new Delayer(iterationDelays[iterationDelayIndex]);
            }
        }
    }

    private void run() {
        new Thread(() -> {
            FpsCalculator fpsCalculator = new FpsCalculator();
            fpsCalculator.beforeProcess();

            delayer = new Delayer(iterationDelays[iterationDelayIndex]);

            simulate = true;
            while (simulate) {
                universe.nextGeneration();

                waitForDisplaying(() -> {
                    sceneController.redrawScene();
                    displayStatistics();
                });

                delayer.delayIteration();

                fpsCalculator.calculateFrames();
                Platform.runLater(() -> fpsText.setText(String.format("%5.2f", fpsCalculator.getFps())));
            }

        }).start();
    }

    private void displayStatistics() {
        generationText.setText(String.valueOf(universe.getGeneration()));
        populationText.setText(String.valueOf(universe.getAllAliveCells().size()));
    }

    private void initWorld() {
        universe = new UniverseFactory().createUniverse(lifeAlgorithm);
        initialLife();
    }

    private void initialLife() {
        universe.clear();
//        universe.setState(Arrays.asList(new Point(0,0)));//new RleParser().parse(R_PENTAMINO));
        universe.setState(new RleParser().parse(R_PENTAMINO));
    }

    private void reset() {
        initialLife();
        sceneController.redrawScene();
        displayStatistics();
    }

    private void waitForDisplaying(Runnable operation) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            operation.run();
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}