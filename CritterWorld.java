// Ankith Kandikonda, avk393
// Donovan McCray, dom342

package  assignment5;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.List;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Rotate;

import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.lang.reflect.Method;
import java.util.Timer;


public class CritterWorld {

    // used for displays, stages and panes
    private Stage window_control;
    private Stage window_critters;
    // used for GUI
    private static GridPane world;
    private static Spinner<Integer> frameField;
    private static Button addCritterBtn;
    private static Button takeStepsBtn;
    private static Button setSeedBtn;
    private static Button clearWorld;
    private static Button closeBtn;
    private static ArrayList<Button> buttonsList;
    private static int worldWidth = 600;
    private static int worldHeight = 600;

    private static int critters_per_row = Params.WORLD_WIDTH;
    private static int critters_per_col = Params.WORLD_HEIGHT;
    private static int critterWidth = (worldWidth/critters_per_row)-10;
    private static int critterHeight = (worldHeight/critters_per_col)-10;

    ComboBox dropdown;
    private static List<Label> statsLabels;
    private static List<CheckBox> statsCheckboxes;

    /* Gets the package name.  The usage assumes that Critter and its
       subclasses are all in the same package. */
    private static String myPackage; // package of Critter file.

    /* Critter cannot be in default pkg. */
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    public CritterWorld() {
        world = new GridPane();
        frameField = new Spinner<>();
        addCritterBtn = new Button();
        takeStepsBtn = new Button();
        setSeedBtn = new Button();
        clearWorld = new Button();
        closeBtn = new Button();

        buttonsList = new ArrayList<>();
        buttonsList.add(addCritterBtn);
        buttonsList.add(takeStepsBtn);
        buttonsList.add(setSeedBtn);
        buttonsList.add(clearWorld);
        buttonsList.add(closeBtn);

        if(Params.WORLD_HEIGHT>40 || Params.WORLD_WIDTH>40){
            worldWidth = 1500;
            worldHeight = 800;
            critterWidth = (worldWidth/critters_per_row)-10;
            critterHeight = (worldHeight/critters_per_col)-10;
        }

        statsLabels = new ArrayList<Label>();
        statsCheckboxes = new ArrayList<CheckBox>();
    }

    public void displayWorld(Stage worldStage) {

    }


    public void initializeWorld(Stage worldStage) {
        window_critters = worldStage;
        window_critters.setTitle("Critters World");

//        paintGridLines(world);
        world.getStyleClass().add("grid");

        int x_dim = (worldWidth/(critters_per_row));
        int y_dim = (worldHeight/(critters_per_col));
        // creating the grid lines to separate critters
        for (int i=0; i<critters_per_row; i++){
            RowConstraints row = new RowConstraints(y_dim);
            world.getRowConstraints().add(row);
        }
        for (int i=0; i<critters_per_col; i++){
            ColumnConstraints col = new ColumnConstraints(x_dim);
            world.getColumnConstraints().add(col);
        }

        // drawing actual grid
        for (int x=0; x<critters_per_row; x++){
            for (int y=0; y<critters_per_col; y++){
                Shape s = new Rectangle(x_dim,y_dim);
                s.setFill(null);
                s.setStroke(Color.ORANGE);
                world.add(s,x,y);
            }
        }

        Scene inputsScene = new Scene(world, worldWidth, worldHeight);
        window_control.setAlwaysOnTop(true);
        window_critters.setScene(inputsScene);
        window_critters.show();
    }

    public void setUpControlsView(Stage primaryStage) {
        window_control = primaryStage;
        window_control.setTitle("Project 5 - Critters");

        int critterChoiceRow = 0;
        int addCrittersRow = 2;
        int takeStepsRow = 3;
        int seedRow = 4;
        int frameRow = 5;
        int clearWorldRow = 6;

        TabPane tabPane = new TabPane();

        // Setup Controls tab
        Tab controlsTab = new Tab("Controls");
        GridPane controlsGrid = new GridPane();
        controlsGrid.setPadding(new Insets(10,10,10,10));
        controlsGrid.setVgap(8);
        controlsGrid.setHgap(8);

        // Dropdown menu for critter choice using user input
        ObservableList<String> crittersList = FXCollections.observableArrayList();
        dropdown = new ComboBox(crittersList);
        TextField manualField = new TextField();
        Label critterStatusLabel = new Label();
        critterStatusLabel.setTextFill(Color.RED);
        critterStatusLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        controlsGrid.add(critterStatusLabel,1,critterChoiceRow+1);
        Label chooseCritterLabel = new Label("Critter (ex. Clover): ");
        chooseCritterLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        controlsGrid.add(chooseCritterLabel,0,critterChoiceRow);
        controlsGrid.add(manualField,1,critterChoiceRow);
        controlsGrid.add(dropdown,2,critterChoiceRow);
        manualField.setOnAction(e-> {
            try {
                Class className = Class.forName(myPackage + "." + manualField.getText());
                if (!crittersList.contains(manualField.getText())) {
                    crittersList.add(manualField.getText());
                    dropdown.setValue(manualField.getText());
                    manualField.setText("");
                    critterStatusLabel.setText("");
                }
            } catch (ClassNotFoundException e1) {
                critterStatusLabel.setText("Invalid Critter: " + manualField.getText());
                manualField.setText("");
            }
        });


        // TextField for adding # of critters
        TextField addCritterField = new TextField();
        addCritterField.setOnAction(e->{
            try {
                int count = Integer.parseInt(addCritterField.getText());
                for (int i = 0; i < count; i++) {
                    Critter.createCritter(dropdown.getValue().toString());
                }
                critterStatusLabel.setText("Added " + count + " " + dropdown.getValue().toString());
            } catch (InvalidCritterException error) {
                critterStatusLabel.setText("Something went wrong.");
            } catch (NumberFormatException error) {
                critterStatusLabel.setText("Please enter a number.");
            } catch (NullPointerException error) {
//                critterStatusLabel.setText("Please choose a critter type.");
            }
        });
        Label numCrittersLabel = new Label("Add no. of critters: ");
        numCrittersLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        controlsGrid.add(numCrittersLabel,0,addCrittersRow);
        controlsGrid.add(addCritterField,1,addCrittersRow);
        addCritterBtn.setText("Add Critter(s)");
        addCritterBtn.setOnAction(e->{
            try {
                int count = Integer.parseInt(addCritterField.getText());
                for (int i = 0; i < count; i++) {
                    Critter.createCritter(dropdown.getValue().toString());
                }
                critterStatusLabel.setText("Added " + count + " " + dropdown.getValue().toString());
            } catch (InvalidCritterException error) {
                critterStatusLabel.setText("Something went wrong.");
            } catch (NumberFormatException error) {
                critterStatusLabel.setText("Please enter a number.");
            } catch (NullPointerException error) {
                critterStatusLabel.setText("Please choose a critter.");
            }
        });
        controlsGrid.add(addCritterBtn,2,addCrittersRow);


        // Take # of steps
        TextField stepsField = new TextField();
        takeStepsBtn.setText("Time Step");
        controlsGrid.add(stepsField,1,takeStepsRow);
        Label stepsFieldLabel = new Label("No. Steps: ");
        stepsFieldLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        controlsGrid.add(stepsFieldLabel,0,takeStepsRow);
        takeStepsBtn.setOnAction(e->{
            try {
                int count = Integer.parseInt(stepsField.getText());
                for (int i = 0; i < count; i++) {
                    Critter.worldTimeStep();
                }
            } catch (NumberFormatException error) {
                System.out.println("Please enter a number.");
                enableControls();
            } catch (NullPointerException error) {
                System.out.println("Take steps error");
                enableControls();
            }
        });
        controlsGrid.add(takeStepsBtn,2,takeStepsRow);


        // Set the seed for the random number generation
        TextField seedField = new TextField();
        Label nLabel = new Label("Seed (integer): ");
        nLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        controlsGrid.add(nLabel,0,seedRow);
        controlsGrid.add(seedField,1,seedRow);
        seedField.setOnAction(e-> {
            try {
                long number = Integer.parseInt(seedField.getText());
                Critter.setSeed(number);
            } catch (NumberFormatException error) {
                System.out.println("Please enter a number.");
            }
        });
        setSeedBtn.setText("Set Seed");
        controlsGrid.add(setSeedBtn,2,seedRow);

        // ClearWorld button
        clearWorld.setText("Clear World");
        controlsGrid.add(clearWorld,0,clearWorldRow);
        clearWorld.setOnAction(e -> Critter.clearWorld());

        // Exit Critter simulation
        closeBtn.setText("Close");
        closeBtn.setOnAction(e->{
            System.exit(0);
        });
        controlsGrid.add(closeBtn,0,11);

        controlsTab.setContent(controlsGrid);

        // Stats Tab
        GridPane statsGrid = new GridPane();
        statsGrid.setPadding(new Insets(10,10,10,10));
        statsGrid.setVgap(8);
        statsGrid.setHgap(8);
        Tab statsTab = new Tab("Stats");
        statsTab.setContent(statsGrid);
        statsTab.setOnSelectionChanged(e-> {
            if (statsTab.isSelected()) {
                updateStats(statsGrid, dropdown);
            }
        });

        // Parameters Tab
        GridPane paramsGrid = new GridPane();
        paramsGrid.setPadding(new Insets(10,10,10,10));
        paramsGrid.setVgap(8);
        paramsGrid.setHgap(8);
        Tab paramTab = new Tab("Parameters");
        setUpParamsGrid(paramsGrid);
        paramTab.setContent(paramsGrid);

        // Animation tab
        GridPane animGrid = new GridPane();
        animGrid.setPadding(new Insets(10,10,10,10));
        animGrid.setVgap(8);
        animGrid.setHgap(8);
        Tab animTab = new Tab("Animate");
        setUpAnimGrid(animGrid);
        animTab.setContent(animGrid);

        // Add tabs
        tabPane.getTabs().add(controlsTab);
        tabPane.getTabs().add(animTab);
        tabPane.getTabs().add(statsTab);
        tabPane.getTabs().add(paramTab);

        tabPane.setOnMouseClicked(e-> {

            // Clear the stats labels when user switches selected tab
            if (!tabPane.getSelectionModel().getSelectedItem().equals(statsTab)) {
                statsGrid.getChildren().clear();
            }

        });


        Scene inputsScene = new Scene(tabPane, 450, 600);
        window_control.setScene(inputsScene);
        window_control.show();
    }

    private void setUpStatsGrid() {
        if (statsLabels.size() != dropdown.getItems().size()) {
            for (int i = statsLabels.size(); i < dropdown.getItems().size(); i++) {
                Label label = new Label("");
                statsLabels.add(label);
            }
        }

        if (statsCheckboxes.size() != dropdown.getItems().size()) {
            for (int i = statsCheckboxes.size(); i < dropdown.getItems().size(); i++) {
                CheckBox checkBox = new CheckBox();
                statsCheckboxes.add(checkBox);
            }
        }
    }

    private void updateStatsLabels() {
        for (int i = 0; i < dropdown.getItems().size(); i++) {
            try {
                if (statsCheckboxes.get(i).isSelected()) {
                    Class className = Class.forName(myPackage + "." + dropdown.getItems().get(i).toString());
                    List<Critter> instances = Critter.getInstances(dropdown.getItems().get(i).toString());

                    Method runStats = className.getMethod("runStats", List.class);
                    Object result = runStats.invoke(Critter.class, instances);

                    statsLabels.get(i).setText(result.toString());
                }
            } catch (NoSuchMethodException e) {
                try {
                    List<Critter> instances;
                    instances = Critter.getInstances(dropdown.getItems().get(i).toString());

                    statsLabels.get(i).setText(Critter.runStats(instances));

                } catch (InvalidCritterException e1) {}

            } catch (InvalidCritterException | ClassNotFoundException | IllegalAccessException | IndexOutOfBoundsException | InvocationTargetException | NullPointerException e) {}
        }
    }

    private void updateStats(GridPane statsGrid, ComboBox dropdown) {

        setUpStatsGrid();

        for (int i = 0; i < dropdown.getItems().size(); i++) {
            statsGrid.add(new Label(dropdown.getItems().get(i).toString()),0,i);
//            CheckBox checkBox = new CheckBox();
            CheckBox checkBox = statsCheckboxes.get(i);
            try {
                Class className = Class.forName(myPackage + "." + dropdown.getItems().get(i).toString());
                List<Critter> instances = Critter.getInstances(dropdown.getItems().get(i).toString());

                Method runStats = className.getMethod("runStats", List.class);
                Object result = runStats.invoke(Critter.class,instances);

//                Label label = new Label("");
                Label label = statsLabels.get(i);
                statsGrid.add(label,2,i);

                if (checkBox.isSelected()) {
                    label.setText(result.toString());
                } else {
                    label.setText("");
                }
                checkBox.setOnAction(e1-> {

                    if (checkBox.isSelected()) {
                        label.setText(result.toString());
                    } else {
                        label.setText("");
                    }

                });

            } catch (NoSuchMethodException e) {
                try {
                    List<Critter> instances;
                    instances = Critter.getInstances(dropdown.getItems().get(i).toString());

//                    Label label = new Label("");
                    Label label = statsLabels.get(i);
                    statsGrid.add(label,2,i);

                    if (checkBox.isSelected()) {
                        label.setText(Critter.runStats(instances));
                    } else {
                        label.setText("");
                    }
                    checkBox.setOnAction(e1-> {
                        if (checkBox.isSelected()) {
                            label.setText(Critter.runStats(instances));
                        } else {
                            label.setText("");
                        }
                    });

                } catch (InvalidCritterException e1) {}

            } catch (InvalidCritterException | ClassNotFoundException | IllegalAccessException | InvocationTargetException | NullPointerException e) {}

            statsGrid.add(checkBox,1,i);
        }
    }

    private void setUpAnimGrid(GridPane animGrid) {

        ToggleButton animButton = new ToggleButton("Start Animation");

        // Frames to be run for each time step
        // Needs to be static to access frames for WorldTimeStep in Critter
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10,1);
        frameField.setValueFactory(valueFactory);
        Label frameLabel = new Label("Frames per step: ");
        frameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        animGrid.add(frameLabel,0,0);
        animGrid.add(frameField,1,0);

        animButton.setOnAction(event -> {
            if (animButton.isSelected()) {
                animButton.setText("Pause Animation");
                animTimer.start();
                // disable GUI controls
                disableControls();
            } else {
                animTimer.stop();
                animButton.setText("Start Animation");
                // re-enable GUI controls
                enableControls();
            }
        });

        animGrid.add(animButton,0,1);
    }

    private void setUpParamsGrid(GridPane paramsGrid) {
        paramsGrid.add(new Label("Walk energy cost"),0,0);
        TextField walkEnergyField = new TextField();
        paramsGrid.add(walkEnergyField,1,0);

        paramsGrid.add(new Label("Run energy cost"),0,1);
        TextField runEnergyField = new TextField();
        paramsGrid.add(runEnergyField,1,1);

        paramsGrid.add(new Label("Rest energy cost"),0,2);
        TextField restEnergyField = new TextField();
        paramsGrid.add(restEnergyField,1,2);

        paramsGrid.add(new Label("Look energy cost"),0,3);
        TextField lookEnergyField = new TextField();
        paramsGrid.add(lookEnergyField,1,3);

        paramsGrid.add(new Label("Min reproduce energy"),0,4);
        TextField minReprEnergyField = new TextField();
        paramsGrid.add(minReprEnergyField,1,4);

        paramsGrid.add(new Label("Refresh clover count"),0,5);
        TextField refreshClovField = new TextField();
        paramsGrid.add(refreshClovField,1,5);

        paramsGrid.add(new Label("Photosynthesis energy"),0,6);
        TextField photosynthEnergyField = new TextField();
        paramsGrid.add(photosynthEnergyField,1,6);

        paramsGrid.add(new Label("Start energy"),0,7);
        TextField startEnergyField = new TextField();
        paramsGrid.add(startEnergyField,1,7);

        walkEnergyField.setOnAction(e-> {
            try {
                Params.WALK_ENERGY_COST = Integer.parseInt(walkEnergyField.getText());
            } catch (NumberFormatException e1) { walkEnergyField.setText(""); }
        });
        runEnergyField.setOnAction(e-> {
            try {
                Params.WALK_ENERGY_COST = Integer.parseInt(walkEnergyField.getText());
            } catch (NumberFormatException e1) { runEnergyField.setText(""); }
        });
        restEnergyField.setOnAction(e-> {
            try {
                Params.WALK_ENERGY_COST = Integer.parseInt(walkEnergyField.getText());
            } catch (NumberFormatException e1) {restEnergyField.setText("");}
        });
        lookEnergyField.setOnAction(e-> {
            try {
                Params.WALK_ENERGY_COST = Integer.parseInt(walkEnergyField.getText());
            } catch (NumberFormatException e1) {lookEnergyField.setText("");}
        });
        minReprEnergyField.setOnAction(e-> {
            try {
                Params.WALK_ENERGY_COST = Integer.parseInt(walkEnergyField.getText());
            } catch (NumberFormatException e1) {minReprEnergyField.setText("");}
        });
        refreshClovField.setOnAction(e-> {
            try {
                Params.WALK_ENERGY_COST = Integer.parseInt(walkEnergyField.getText());
            } catch (NumberFormatException e1) {refreshClovField.setText("");}
        });
        photosynthEnergyField.setOnAction(e-> {
            try {
                Params.WALK_ENERGY_COST = Integer.parseInt(walkEnergyField.getText());
            } catch (NumberFormatException e1) {photosynthEnergyField.setText("");}
        });
        startEnergyField.setOnAction(e-> {
            try {
                Params.WALK_ENERGY_COST = Integer.parseInt(walkEnergyField.getText());
            } catch (NumberFormatException e1) {startEnergyField.setText("");}
        });
    }

    public static Shape addCritter(Critter critter, int x, int y, boolean draw){
        Shape shape = null;
        //creating shape
        switch (critter.viewShape()){
            case CIRCLE:
                shape = new Circle(critterWidth/2);
                break;
            case SQUARE:
                // ***will need to adjust this
                // dimensions used won't make a square is Params height and width are different
                shape = new Rectangle(critterWidth,critterHeight);
                break;
            case TRIANGLE:
                shape = new Polygon();
                ((Polygon)shape).getPoints().addAll(new Double[]{
                        0.0,                        (double)critterWidth/2,
                        (double) critterHeight,     (double)critterWidth,
                        (double) critterHeight,     0.0
                });
                break;
            case STAR:
                shape = new Polygon();
                ((Polygon)shape).getPoints().addAll(new Double[]{
                        0.0,                        (double)critterWidth/2,
                        (double) critterHeight/2,   (double)critterWidth,
                        (double) critterHeight,     (double)critterWidth,
                        (double) critterHeight,     (double) critterWidth/3,
                        (double) critterHeight/2,   0.0

                });
                break;
            case DIAMOND:
                shape = new Polygon();
                ((Polygon)shape).getPoints().addAll(new Double[]{
                        0.0,                        (double)critterWidth/2,
                        (double) critterHeight/2,   (double)critterWidth,
                        (double) critterHeight,     (double)critterWidth/2,
                        (double) critterHeight/2,   0.0
                });
                break;
            default:
                shape = null;
        }

        try {
            shape.setFill(critter.viewFillColor());
            shape.setStroke(critter.viewOutlineColor());
            // aligning shape and adding to grid
            GridPane.setHalignment(shape, HPos.CENTER);
            GridPane.setValignment(shape, VPos.CENTER);
            if(draw) world.add(shape, x, y);
        }
        catch (NullPointerException e) {
            // ***tell user critter was not added to GUI
            System.out.println("Caught exception in addCritter, shape not initialized");
        }

        return shape;
    }

    public static int getFrames () {
        try {
            return frameField.getValue();
        }
        catch (Exception e) {
            // frameLabel.setText("Enter valid number of frames");
            return 0;
        }
    }

    public static void drawCritter(Shape shape, int x, int y){
        try {
//            world.add(shape, x, y);
            if (!world.getChildren().contains(shape)) {
//                world.getChildren().add(shape);
                world.add(shape, x, y);
            }
            else {
                eraseCritter(shape);
                world.add(shape, x, y);
            }
        } catch (IllegalArgumentException e) {
            // for debugging purposes
            System.out.println("Caught exception in drawCritter");
        } catch (NullPointerException e) {
            System.out.println(shape.toString());
        }
    }

    public static void eraseCritter(Shape shape){
        try {
            world.getChildren().remove(shape);
        }
        catch (IllegalArgumentException e) {
            // for debugging purposes
            System.out.println("Caught exception in eraseCritter");
        }
    }

    public static void disableControls() {
        for (Button button : buttonsList) button.setDisable(true);
    }

    public static void enableControls() {
        for (Button button : buttonsList) button.setDisable(false);
    }

    public AnimationTimer animTimer = new AnimationTimer() {
        private long lastUpdate = 0;
        @Override
        public void handle(long now) {
            // max long = 1_000_000_000
            if (now - lastUpdate >= 100_000_000*frameField.getValue()) {
                // Perform time steps
                Critter.worldTimeStep();
                lastUpdate = now ;
                updateStatsLabels();
            }
        }
    };

}