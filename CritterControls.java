//package assignment5;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.GridPane;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.stage.Stage;
//
//public class CritterControls {
//
//    public CritterControls() {
//
//    }
//
//    public void setUpControlsView(Stage primaryStage) {
//        window_control = primaryStage;
//        window_control.setTitle("Project 5 - Critters");
//
//        int critterChoiceRow = 0;
//        int addCrittersRow = 2;
//        int takeStepsRow = 3;
//        int seedRow = 4;
//        int frameRow = 5;
//        int clearWorldRow = 6;
//
//        TabPane tabPane = new TabPane();
//
//        // Setup Controls tab
//        Tab controlsTab = new Tab("Controls");
//        GridPane controlsGrid = new GridPane();
//        controlsGrid.setPadding(new Insets(10,10,10,10));
//        controlsGrid.setVgap(8);
//        controlsGrid.setHgap(8);
//
//        // Dropdown menu for critter choice using user input
//        ObservableList<String> crittersList = FXCollections.observableArrayList();
//        dropdown = new ComboBox(crittersList);
//        TextField manualField = new TextField();
//        Label critterStatusLabel = new Label();
//        critterStatusLabel.setTextFill(Color.RED);
//        critterStatusLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
//
//        controlsGrid.add(critterStatusLabel,1,critterChoiceRow+1);
//        Label chooseCritterLabel = new Label("Critter (ex. Clover): ");
//        chooseCritterLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
//        controlsGrid.add(chooseCritterLabel,0,critterChoiceRow);
//        controlsGrid.add(manualField,1,critterChoiceRow);
//        controlsGrid.add(dropdown,2,critterChoiceRow);
//        manualField.setOnAction(e-> {
//            try {
//                Class className = Class.forName(myPackage + "." + manualField.getText());
//                if (!crittersList.contains(manualField.getText())) {
//                    crittersList.add(manualField.getText());
//                    dropdown.setValue(manualField.getText());
//                    manualField.setText("");
//                    critterStatusLabel.setText("");
//                }
//            } catch (ClassNotFoundException e1) {
//                critterStatusLabel.setText("Invalid Critter: " + manualField.getText());
//                manualField.setText("");
//            }
//        });
//
//
//        // TextField for adding # of critters
//        TextField addCritterField = new TextField();
//        addCritterField.setOnAction(e->{
//            try {
//                int count = Integer.parseInt(addCritterField.getText());
//                for (int i = 0; i < count; i++) {
//                    Critter.createCritter(dropdown.getValue().toString());
//                }
//                critterStatusLabel.setText("Added " + count + " " + dropdown.getValue().toString());
//            } catch (InvalidCritterException error) {
//                critterStatusLabel.setText("Something went wrong.");
//            } catch (NumberFormatException error) {
//                critterStatusLabel.setText("Please enter a number.");
//            } catch (NullPointerException error) {
////                critterStatusLabel.setText("Please choose a critter type.");
//            }
//        });
//        Label numCrittersLabel = new Label("Add no. of critters: ");
//        numCrittersLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
//        controlsGrid.add(numCrittersLabel,0,addCrittersRow);
//        controlsGrid.add(addCritterField,1,addCrittersRow);
//        addCritterBtn.setText("Add Critter(s)");
//        addCritterBtn.setOnAction(e->{
//            try {
//                int count = Integer.parseInt(addCritterField.getText());
//                for (int i = 0; i < count; i++) {
//                    Critter.createCritter(dropdown.getValue().toString());
//                }
//                critterStatusLabel.setText("Added " + count + " " + dropdown.getValue().toString());
//            } catch (InvalidCritterException error) {
//                critterStatusLabel.setText("Something went wrong.");
//            } catch (NumberFormatException error) {
//                critterStatusLabel.setText("Please enter a number.");
//            } catch (NullPointerException error) {
//                critterStatusLabel.setText("Please choose a critter.");
//            }
//        });
//        controlsGrid.add(addCritterBtn,2,addCrittersRow);
//
//
//        // Take # of steps
//        TextField stepsField = new TextField();
//        takeStepsBtn.setText("Time Step");
//        controlsGrid.add(stepsField,1,takeStepsRow);
//        Label stepsFieldLabel = new Label("No. Steps: ");
//        stepsFieldLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
//        controlsGrid.add(stepsFieldLabel,0,takeStepsRow);
//        takeStepsBtn.setOnAction(e->{
//            try {
//                int count = Integer.parseInt(stepsField.getText());
//                for (int i = 0; i < count; i++) {
//                    Critter.worldTimeStep();
//                }
//            } catch (NumberFormatException error) {
//                System.out.println("Please enter a number.");
//                enableControls();
//            } catch (NullPointerException error) {
//                System.out.println("Take steps error");
//                enableControls();
//            }
//        });
//        controlsGrid.add(takeStepsBtn,2,takeStepsRow);
//
//
//        // Set the seed for the random number generation
//        TextField seedField = new TextField();
//        Label nLabel = new Label("Seed (integer): ");
//        nLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
//        controlsGrid.add(nLabel,0,seedRow);
//        controlsGrid.add(seedField,1,seedRow);
//        seedField.setOnAction(e-> {
//            try {
//                long number = Integer.parseInt(seedField.getText());
//                Critter.setSeed(number);
//            } catch (NumberFormatException error) {
//                System.out.println("Please enter a number.");
//            }
//        });
//        setSeedBtn.setText("Set Seed");
//        controlsGrid.add(setSeedBtn,2,seedRow);
//
//        // ClearWorld button
//        clearWorld.setText("Clear World");
//        controlsGrid.add(clearWorld,0,clearWorldRow);
//        clearWorld.setOnAction(e -> Critter.clearWorld());
//
//        // Exit Critter simulation
//        closeBtn.setText("Close");
//        closeBtn.setOnAction(e->{
//            System.exit(0);
//        });
//        controlsGrid.add(closeBtn,0,11);
//
//        controlsTab.setContent(controlsGrid);
//
//        // Stats Tab
//        GridPane statsGrid = new GridPane();
//        statsGrid.setPadding(new Insets(10,10,10,10));
//        statsGrid.setVgap(8);
//        statsGrid.setHgap(8);
//        Tab statsTab = new Tab("Stats");
//        statsTab.setContent(statsGrid);
//        statsTab.setOnSelectionChanged(e-> {
//            if (statsTab.isSelected()) {
//                updateStats(statsGrid, dropdown);
//            }
//        });
//
//        // Parameters Tab
//        GridPane paramsGrid = new GridPane();
//        paramsGrid.setPadding(new Insets(10,10,10,10));
//        paramsGrid.setVgap(8);
//        paramsGrid.setHgap(8);
//        Tab paramTab = new Tab("Parameters");
//        setUpParamsGrid(paramsGrid);
//        paramTab.setContent(paramsGrid);
//
//        // Animation tab
//        GridPane animGrid = new GridPane();
//        animGrid.setPadding(new Insets(10,10,10,10));
//        animGrid.setVgap(8);
//        animGrid.setHgap(8);
//        Tab animTab = new Tab("Animate");
//        setUpAnimGrid(animGrid);
//        animTab.setContent(animGrid);
//
//        // Add tabs
//        tabPane.getTabs().add(controlsTab);
//        tabPane.getTabs().add(animTab);
//        tabPane.getTabs().add(statsTab);
//        tabPane.getTabs().add(paramTab);
//
//        tabPane.setOnMouseClicked(e-> {
//
//            // Clear the stats labels when user switches selected tab
//            if (!tabPane.getSelectionModel().getSelectedItem().equals(statsTab)) {
//                statsGrid.getChildren().clear();
//            }
//
//        });
//
//
//        Scene inputsScene = new Scene(tabPane, 450, 600);
//        window_control.setScene(inputsScene);
//        window_control.show();
//    }
//
//}