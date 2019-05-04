
package gravitationalintegrator.ui;

import gravitationalintegrator.domain.Body;
import gravitationalintegrator.domain.IntegratorHandler;
import gravitationalintegrator.domain.IntegratorTask;

import java.io.File;
import java.util.concurrent.ExecutionException;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    Text sysHeader;
    Menu fileMenu;
    Menu helpMenu;
    MenuBar menuBar;
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
        dialogue.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        
        fileMenu = new Menu("File");
        
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
        
        fileMenu.getItems().add(loadNewData);
        fileMenu.getItems().add(loadOldData);
        fileMenu.getItems().add(saveSim);
        
        helpMenu = new Menu("Help");
        
        MenuItem controls = new MenuItem("Controls");
        controls.setOnAction((event) -> {
            helpDialogue(2);
        });
        
        MenuItem formatting = new MenuItem("File Formating");
        formatting.setOnAction((event) -> {
            helpDialogue(1);    
        });        
        
        helpMenu.getItems().add(controls);
        helpMenu.getItems().add(formatting);
        
        menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(helpMenu);

        //Add text to tell current time of the step to user 
        sysHeader = new Text("Time of currently displayed step: "
                + intHandler.getCurrT() / (24 * 3600) + "d," 
                + " number if integrated steps: " + intHandler.getStepCount());
        
        //Call builder to construct table
        tableBuilder();
        
        VBox dataScene = new VBox(menuBar);
        dataScene.setPadding(new Insets(5.0, 5.0, 5.0, 5.0 ));
        dataScene.setSpacing(5.0);
        dataScene.getChildren().add(sysHeader);
        dataScene.getChildren().add(table);

        //Add input for total simulation time and timestep, run button
        HBox simControl = new HBox();
        simControl.setSpacing(5.0);
        
        ProgressBar intProg = new ProgressBar(0.0);
        intProg.setPrefWidth(600);
        
        Button run = new Button("Run");
        run.setPrefWidth(75);
        run.setOnAction((event) -> {
            //check if old integration is still running
            if (intRunning()) {
                return;
            }
            
            //Check that simulation values are usable
            if (intHandler.isSysEmpty()) {
                errorDialogue("No bodies to integrate");
            } else if (!intHandler.areTimesOk()) {
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
                        
                        sysHeader.setText("Time of currently displayed step: " 
                                + intHandler.getCurrT() / (24 * 3600) + "d," 
                                + " number if integrated steps: " + intHandler.getStepCount());
                        
                        dialogue.setAlertType(AlertType.INFORMATION);
                        dialogue.setContentText("Integration completed");
                        dialogue.setTitle("Success!");
                        dialogue.setHeaderText("Success!");
                        dialogue.show(); 
                        
                        intTask = null;
                        
                    } catch (InterruptedException | ExecutionException err) {
                        errorDialogue(err.getMessage());
                    }
                });
                
                //Tie task to progress bar
                intProg.progressProperty().bind(intTask.progressProperty());
                
                //Ask intHandler to setup and run a thrad for intTask
                intHandler.startInt();
            }
        });
        
        //Integration controls
        Label totalTLabel = new Label("T (d):");
        totalTLabel.setPrefWidth(35);
        Label deltaTLabel = new Label("dT (d):");  
        deltaTLabel.setPrefWidth(35);
        
        TextField totalTField = new TextField();
        totalTField.setPrefWidth(100);
        totalTField.setText(Double.toString(intHandler.getTotalT() / (24 * 3600)));
        
        TextField deltaTField = new TextField();
        deltaTField.setPrefWidth(100);
        deltaTField.setText(Double.toString(intHandler.getDeltaT() / (24 * 3600)));
        
        //button for submitting total runtime and timestep, shows error if 
        //unable to parse as double, or value not usable
        Button submit = new Button("Submit");
        submit.setPrefWidth(75);
        submit.setOnAction((event) -> {
            if (intRunning()) {
                return;
            }
            
            try {
                double tempT = Double.parseDouble(totalTField.getText()) * 24 * 3600;
               
                if (tempT <= 0.0) {
                    errorDialogue("T-value must be greater than 0");
                } else {
                    try {
                        double tempDT = Double.parseDouble(deltaTField.getText()) * 24 * 3600;
                        
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
        dialogue.setTitle("Error!");
        dialogue.setHeaderText("Error!");
        dialogue.show();     
    }
    
    private void helpDialogue(int type) {
        dialogue.setAlertType(AlertType.INFORMATION);
        String form = "Input file rows: \n\n" +
                "GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0\n" +
                "GM_1 x_1 y_1 z_1 vx_1 vy_1 vz_1\n" +
                "...\n\n" + 
                "where GM is standard gravitational parameter" + 
                " of body, in units km^3/s^2, x,y,z are location" + 
                " vector components in km, and vx,vy,vz velocity" + 
                " componets in km/s.\n\n" + 
                "Output file will follow form: \n\n" +
                "nSteps nBodies\n" +
                "T1 GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0 GM_1 x_1...\n" +
                "T2 GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0 GM_1 x_1...\n" +
                "...\n\n" +
                "where nSteps is the number of integration steps and initial" +
                " step, and nBodies is number of bodies in integrated system" +
                ", T:s are times at timestep in seconds, rest as" + 
                " in input file. Loading old integrations uses same" + 
                " formatting as output.";

        String cont = "Loading a new system to integrate:\n" + 
                "File-menu -> Load data\n\n" + 
                "Loading old integration to continue:\n" +
                "File-menu -> Load simulation\n\n" +
                "Saving simulation steps:\n" + 
                "File-menu -> Save simulation\n\n" + 
                "Setting integration timespan and timestep, insert span to " + 
                "tesxbox marked T, timestep to box dT, confirm by pressing " +
                "Submit-button, T can be smaller than dT or not exactly" + 
                " divisible by dT, in such cases time of last integrated step" +
                " will be within dT above T.\n\n" + 
                "Once something to simulate has been loaded in and timevalues" + 
                " are set integration can be started by pressing Run-button.";
                
        switch (type) {
            case 1: 
                dialogue.setContentText(form);
                dialogue.setTitle("Formatting");
                dialogue.setHeaderText("File formatting");
                dialogue.show();
                break;
            case 2:
                dialogue.setContentText(cont);
                dialogue.setTitle("Controls");
                dialogue.setHeaderText("Program controls");
                dialogue.show();
                break;
        }
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
