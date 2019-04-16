
package gravitationalintegrator.ui;

import gravitationalintegrator.domain.Body;
import gravitationalintegrator.domain.FileHandler;
import gravitationalintegrator.domain.Integrator;
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
 

public class UI extends Application {
    IntegratorTask intTask;
    Integrator leapfrog;
    Sys sys;
    TableView table;
    Menu menu;
    MenuBar menuBar;
    ArrayList<Sys> steps;
    Alert dialogue;
    double totalT;
    double deltaT;

    
    @Override
    public void init() {
        totalT = 0.0;
        deltaT = 0.0; 
    }
    
    @Override
    public void start(Stage stage) {
        dialogue = new Alert(AlertType.NONE);
        
        menu = new Menu("File");
        
        //Menu item to open file with the parameters for bodies to integrate
        MenuItem loadNewData = new MenuItem("Load data");
        loadNewData.setOnAction((event) -> {
            //check if old integration is still running
            if(intRunning()){
                return;
            }
            
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            
            if (file != null) {
                try {
                    sys = FileHandler.readFromFile(file);
                    table.setItems(FXCollections.observableArrayList(sys.getBodies()));
                } catch (Exception err) {
                    errorDialogue(err.getMessage());
                }    
            }
        });
        
        //ToDo
        MenuItem loadOldSim = new MenuItem("Load simulation");
        
        //Menu item for saving and to open file explorer for selection of save target
        MenuItem saveSim = new MenuItem("Save simulation");
        saveSim.setOnAction((event) -> {
            //check if old integration is still running
            if(intRunning()){
                return;
            }
            
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            
            if (file != null) {
                try {
                    FileHandler.writeToFile(steps, file);
                } catch (IOException err) {
                    errorDialogue(err.getMessage());
                }
            }            
        });
        
        menu.getItems().add(loadNewData);
        menu.getItems().add(loadOldSim);
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
            if(intRunning()){
                return;
            }
            
            //Check that simulation values are usable
            if (sys == null) {
                errorDialogue("No bodies to integrate");
            } else if (totalT <= 0.0) {
                errorDialogue("Unacceptabel total runtime value");
            } else if (deltaT <= 0.0) {
                errorDialogue("Unacceptable dT-value");
            //Continue to creating a Task for running the integration, without
            //freezing ui
            } else {         
                intTask = new IntegratorTask(totalT, deltaT, sys);
                
                //On succes update values on GUI to final step, collect list of
                //copies of sytem state on timesteps
                intTask.setOnSucceeded(success -> {
                    try {
                        //If steps list is null initialize it, else add latest 
                        //integration result at the back of former results,
                        //removing last element which would be a duplicate
                        if (steps == null) {
                            steps = new ArrayList<>();
                        } else {
                            steps.remove(steps.size() - 1);
                        }
                        steps.addAll(intTask.get());
                        
                        //sys-object is always the last timestep
                        table.setItems(FXCollections.observableArrayList(sys.getBodies()));
                        
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
        totalTField.setText(Double.toString(totalT));
        
        TextField deltaTField = new TextField();
        deltaTField.setText(Double.toString(deltaT));
        
        //button for submitting total runtime and timestep, shows error if 
        //unable to parse as double, or value not usable
        Button submit = new Button("Submit");
        submit.setOnAction((event) -> {
            if(intRunning()){
                return;
            }
            
            try{
                double tempT = Double.parseDouble(totalTField.getText());
               
                if(tempT <= 0.0){
                    errorDialogue("T-value must be greater than 0");
                }else{
                    try{
                        double tempDT = Double.parseDouble(deltaTField.getText());
                        
                        if(tempDT <= 0.0){
                            errorDialogue("T-value must be greater than 0");
                        }else{
                            totalT = tempT;
                            deltaT = tempDT;
                        }  
                        
                    }catch (NumberFormatException err) {
                        errorDialogue("Unable to parse dT-value as double");
                    }                    
                }
            }catch (NumberFormatException err) {
                errorDialogue("Unable to parse T-value as double");
            }
        

        });
                
        simControl.getChildren().addAll(totalTLabel, totalTField, deltaTLabel, deltaTField, submit, run, intProg);
        
        dataScene.getChildren().add(simControl);
        
        Scene scene = new Scene(dataScene);
        stage.setScene(scene);
        stage.show();
    }
    
    //Error dialogue
    private void errorDialogue(String msg) {
        dialogue.setAlertType(AlertType.ERROR);
        dialogue.setContentText(msg);
        dialogue.show();     
    }
    
    //Checks if Task intTask exosts and is running, for disabling users abilty to
    //change anything and potentially causing unvanted behaviour  
    private boolean intRunning() {
        if(intTask != null && intTask.isRunning()){
            errorDialogue("Last integration still running");
            return true;
        }
        
        return false;
    }
    
    //Utility for building the table with body parameter.
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
