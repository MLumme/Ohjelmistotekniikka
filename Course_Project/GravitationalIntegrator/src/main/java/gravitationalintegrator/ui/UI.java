
package gravitationalintegrator.ui;

import gravitationalintegrator.domain.Body;
import gravitationalintegrator.domain.IntegratorHandler;
import gravitationalintegrator.io.FileHandler;
import gravitationalintegrator.domain.IntegratorTask;
import gravitationalintegrator.domain.Sys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
 
/**
 * Main UI class
 */
public class UI extends Application {
    IntegratorHandler intHandler;
    IntegratorTask intTask;
    TableView table;
    Menu menu;
    MenuBar menuBar;
    ArrayList<Sys> steps;
    Alert dialogue;

    
    @Override
    public void init() {
        intHandler = new IntegratorHandler();
    }
    
    /**
     * start-function to run as JavaFx-application
     * @param stage stage to which application is rendered to
     */
    @Override
    public void start(Stage stage) {
        dialogue = new Alert(AlertType.NONE);
        
        menu = new Menu("File");
        
        //Menu item to open file with the parameters for bodies to integrate
        MenuItem loadNewData = new MenuItem("Load data");
        loadNewData.setOnAction((event) -> {
            //check if old integration is still running
            if (intRunning()) {
                return;
            }
            
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            
            if (file != null) {
                try {
                    intHandler.loadSys(file);
                    table.setItems(FXCollections.observableArrayList(intHandler.getCurrBodies()));
                } catch (Exception err) {
                    errorDialogue(err.getMessage());
                }    
            }
        });
        
        //Load steps for old integration from file
        MenuItem loadOldData = new MenuItem("Load simulation");
            loadOldData.setOnAction((event) -> {
            //check if old integration is still running
            if (intRunning()) {
                return;
            }
            
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            
            if (file != null) {
                try {
                    intHandler.loadSteps(file);
                    table.setItems(FXCollections.observableArrayList(intHandler.getCurrBodies()));
                } catch (Exception err) {
                    errorDialogue(err.getMessage());
                }    
            }
        });
                
        //Menu item for saving and to open file explorer for selection of save target
        MenuItem saveSim = new MenuItem("Save simulation");
        saveSim.setOnAction((event) -> {
            //check if old integration is still running
            if (intRunning()) {
                return;
            }
            
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            
            if (file != null) {
                try {
                    intHandler.saveSteps(file);
                } catch (Exception err) {
                    errorDialogue(err.getMessage());
                }
            }            
        });
        
        menu.getItems().add(loadNewData);
        menu.getItems().add(loadOldData);
        menu.getItems().add(saveSim);
        
        menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        tableBuilder();
        
        VBox dataScene = new VBox(menuBar);
        dataScene.getChildren().add(table);

        //Add input for total simulation time and timestep, run button
        HBox simControl = new HBox();
        ProgressBar intProg = new ProgressBar(0.0);
        
        Button run = new Button("Run");
        run.setOnAction((event) -> {
            //check if old integration is still running
            if (intRunning()) {
                return;
            }
            
            //Check that simulation values are usable
            if (intHandler.isSysEmpty()) {
                errorDialogue("No bodies to integrate");
            } else if (intHandler.areTimesOk()) {
                errorDialogue("Unacceptable time values");
            //Continue to creating a Task for running the integration, without
            //freezing ui
            } else {
                intTask = intHandler.buildTask();
                
                //On succes update values on GUI to final step, collect list of
                //copies of sytem state on timesteps
                intTask.setOnSucceeded(success -> {
                    try {
                        //If steps list is empty add timesteps to it directly, 
                        //else add latest integration result at the back of the
                        //former results,removing last element which would be a 
                        //duplicate
                        intHandler.postInt();
                       
                        //sys-object is always the last timestep
                        table.setItems(FXCollections.observableArrayList(intHandler.getCurrBodies()));
                        
                        dialogue.setAlertType(AlertType.INFORMATION);
                        dialogue.setContentText("Integration completed");
                        dialogue.show(); 
                        
                    } catch (InterruptedException | ExecutionException err) {
                        errorDialogue(err.getMessage());
                    }
                });
                
                //Tie task to progress bar
                intProg.progressProperty().bind(intTask.progressProperty());

                //Thread for running the integration
                Thread intThread = new Thread(intTask);
                intThread.setDaemon(true);
                intThread.start();
            }
        });
        
        //Integration controls
        Label totalTLabel = new Label("T (s):");
        Label deltaTLabel = new Label("dT (s):");  
        
        TextField totalTField = new TextField();
        totalTField.setText(Double.toString(intHandler.getTotalT()));
        
        TextField deltaTField = new TextField();
        deltaTField.setText(Double.toString(intHandler.getDeltaT()));
        
        //button for submitting total runtime and timestep, shows error if 
        //unable to parse as double, or value not usable
        Button submit = new Button("Submit");
        submit.setOnAction((event) -> {
            if (intRunning()) {
                return;
            }
            
            try {
                double tempT = Double.parseDouble(totalTField.getText());
               
                if (tempT <= 0.0) {
                    errorDialogue("T-value must be greater than 0");
                } else {
                    try {
                        double tempDT = Double.parseDouble(deltaTField.getText());
                        
                        if (tempDT <= 0.0) {
                            errorDialogue("T-value must be greater than 0");
                        } else {
                            intHandler.setTs(tempT, tempDT);
                        }  
                        
                    } catch (NumberFormatException err) {
                        errorDialogue("Unable to parse dT-value as double");
                    }                    
                }
            } catch (NumberFormatException err) {
                errorDialogue("Unable to parse T-value as double");
            }
        

        });
                
        simControl.getChildren().addAll(totalTLabel, totalTField, deltaTLabel, deltaTField, submit, run, intProg);
        
        dataScene.getChildren().add(simControl);
        
        Scene scene = new Scene(dataScene);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Function shows JavaFx-Dialogue of the Error-type
     * @param msg string containing the message to show
     */
    private void errorDialogue(String msg) {
        dialogue.setAlertType(AlertType.ERROR);
        dialogue.setContentText(msg);
        dialogue.show();     
    }
    
    /**
     * Checks if Task intTask exists and is running, for disabling users ability to
     * change anything and potentially causing unexpected behaviour  
     * @return Boolean telling if task running or not
     */
    private boolean intRunning() {
        if (intTask != null && intTask.isRunning()) {
            errorDialogue("Last integration still running");
            return true;
        }
        
        return false;
    }
    
    /**
     *Utility for building the table for gravitational systems bodies parameters. 
     */
    private void tableBuilder() {
        table = new TableView();
        
        TableColumn gmCol = new TableColumn("GM");
        TableColumn xCol = new TableColumn("X (km)");
        TableColumn yCol = new TableColumn("Y (km)");
        TableColumn zCol = new TableColumn("Z (km)");
        TableColumn vxCol = new TableColumn("Vx (km/s)");
        TableColumn vyCol = new TableColumn("Vy (km/s)");
        TableColumn vzCol = new TableColumn("Vz (km/s)");
        
        table.getColumns().addAll(gmCol, xCol, yCol, zCol, vxCol, vyCol, vzCol);
        
        gmCol.setMinWidth(150);
        xCol.setMinWidth(150);
        yCol.setMinWidth(150);
        zCol.setMinWidth(150);
        vxCol.setMinWidth(150);
        vyCol.setMinWidth(150);
        vzCol.setMinWidth(150);
        
        gmCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Body, String>, ObservableValue<Double>>() {
            @Override
            public ObjectProperty<Double> call(TableColumn.CellDataFeatures<Body, String> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getGm());
            }
        });
        
        xCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Body, String>, ObservableValue<Double>>() {
            @Override
            public ObjectProperty<Double> call(TableColumn.CellDataFeatures<Body, String> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getLoc().getEntry(0));
            }
        });
        
        yCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Body, String>, ObservableValue<Double>>() {
            @Override
            public ObjectProperty<Double> call(TableColumn.CellDataFeatures<Body, String> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getLoc().getEntry(1));
            }
        });

        zCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Body, String>, ObservableValue<Double>>() {
            @Override
            public ObjectProperty<Double> call(TableColumn.CellDataFeatures<Body, String> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getLoc().getEntry(2));
            }
        });

        vxCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Body, String>, ObservableValue<Double>>() {
            @Override
            public ObjectProperty<Double> call(TableColumn.CellDataFeatures<Body, String> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getVel().getEntry(0));
            }
        });   
        
        vyCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Body, String>, ObservableValue<Double>>() {
            @Override
            public ObjectProperty<Double> call(TableColumn.CellDataFeatures<Body, String> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getVel().getEntry(1));
            }
        });          

        vzCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Body, String>, ObservableValue<String>>() {
            @Override
            public ObjectProperty<String> call(TableColumn.CellDataFeatures<Body, String> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getVel().getEntry(2));
            }
        });
    }
}
