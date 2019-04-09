
package gravitationalintegrator.ui;

import gravitationalintegrator.domain.Body;
import gravitationalintegrator.domain.FileHandler;
import gravitationalintegrator.domain.Integrator;
import gravitationalintegrator.domain.Sys;

import java.io.File;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
 

public class UI extends Application {
    Integrator leapfrog;
    Sys sys;
    TableView table;
    Menu menu;
    ArrayList<Sys> history;
    double totalT;
    double deltaT;

    
    @Override
    public void init() {
        history = new ArrayList<>();
        totalT = 0.0;
        deltaT = 0.0;        
    }
    
    @Override
    public void start(Stage stage) {
        //Menu//////////////////////////////////////////////////////////////////
        menu = new Menu("File");
        
        MenuItem loadNewData = new MenuItem("Load data");
        loadNewData.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            
            if (file != null) {
                sys = FileHandler.readFromFile(file);
                table.setItems(FXCollections.observableArrayList(sys.getBodies()));
            }
        });
        
        MenuItem loadOldSim = new MenuItem("Load simulation");
        MenuItem saveSim = new MenuItem("Save simulation");
        
        
        menu.getItems().add(loadNewData);
        menu.getItems().add(loadOldSim);
        menu.getItems().add(saveSim);
        
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        
        VBox dataScene = new VBox(menuBar);
        //Table for data////////////////////////////////////////////////////////
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
        
        gmCol.setCellValueFactory(new Callback<CellDataFeatures<Body, String>, ObservableValue<Double>>() {
            @Override
            public ObjectProperty<Double> call(CellDataFeatures<Body, String> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getGm());
            }
        });
        
        xCol.setCellValueFactory(new Callback<CellDataFeatures<Body, String>, ObservableValue<Double>>() {
            @Override
            public ObjectProperty<Double> call(CellDataFeatures<Body, String> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getLoc().getEntry(0));
            }
        });
        
        yCol.setCellValueFactory(new Callback<CellDataFeatures<Body, String>, ObservableValue<Double>>() {
            @Override
            public ObjectProperty<Double> call(CellDataFeatures<Body, String> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getLoc().getEntry(1));
            }
        });

        zCol.setCellValueFactory(new Callback<CellDataFeatures<Body, String>, ObservableValue<Double>>() {
            @Override
            public ObjectProperty<Double> call(CellDataFeatures<Body, String> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getLoc().getEntry(2));
            }
        });

        vxCol.setCellValueFactory(new Callback<CellDataFeatures<Body, String>, ObservableValue<Double>>() {
            @Override
            public ObjectProperty<Double> call(CellDataFeatures<Body, String> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getVel().getEntry(0));
            }
        });   
        
        vyCol.setCellValueFactory(new Callback<CellDataFeatures<Body, String>, ObservableValue<Double>>() {
            @Override
            public ObjectProperty<Double> call(CellDataFeatures<Body, String> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getVel().getEntry(1));
            }
        });          

        vzCol.setCellValueFactory(new Callback<CellDataFeatures<Body, String>, ObservableValue<String>>() {
            @Override
            public ObjectProperty<String> call(CellDataFeatures<Body, String> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getVel().getEntry(2));
            }
        });  
        
        dataScene.getChildren().add(table);

        //Add input for total simulation time and timestep, run button
        HBox simControl = new HBox();
        ProgressBar intProg = new ProgressBar(0.0);
        
        Button run = new Button("Run");
        
        run.setOnAction((event) -> {
            if (deltaT <= 0.0) {
                System.out.println("Unacceptable dT-value");
            } else if (totalT <= 0.0) {
                System.out.println("Unacceptabel total runtime value");
            } else if (sys == null) {
                System.out.println("No bodies to integrate");
            } else {
                double curT = sys.getT(); 
                double contT = curT + totalT;
                leapfrog = new Integrator(deltaT, sys);
                
                while(curT < contT) {
                    leapfrog.updateOne();
                    curT = sys.getT();
                }
                
                table.setItems(FXCollections.observableArrayList(sys.getBodies()));
            }
        });
        
        Label totalTLabel = new Label("T (s):");
        Label deltaTLabel = new Label("dT (s):");        
        TextField totalTField = new TextField();
        TextField deltaTField = new TextField();
        
        Button submit = new Button("Submit");
        submit.setOnAction((event) -> {
            totalT = Double.parseDouble(totalTField.getText());
            deltaT = Double.parseDouble(deltaTField.getText());
        });
                
        simControl.getChildren().addAll(totalTLabel, totalTField, deltaTLabel, deltaTField, submit, run, intProg);
        
        dataScene.getChildren().add(simControl);
        
        Scene scene = new Scene(dataScene);
        stage.setScene(scene);
        stage.show();
    }
}
