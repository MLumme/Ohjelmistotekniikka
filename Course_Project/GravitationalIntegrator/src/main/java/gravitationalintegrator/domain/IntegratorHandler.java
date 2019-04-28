
package gravitationalintegrator.domain;

import gravitationalintegrator.io.FileHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class IntegratorHandler {
    double deltaT;
    double totalT;
    private Sys sys;
    private ArrayList<Sys> steps;
    private IntegratorTask intTask;

    public IntegratorHandler() {
        this.deltaT = 0.0;
        this.deltaT = 0.0;
    }

    public double getTotalT() {
        return totalT;
    }

    public double getDeltaT() {
        return deltaT;
    }
  
    /**
     * Takes time parameters from caller and sets totalT and deltaT 
     * @param totalT Total runtime of integration
     * @param deltaT Integration timestep
     */
    public void setTs(double totalT, double deltaT) {
        this.totalT = totalT;
        this.deltaT = deltaT;
    }
  
    
    public Sys getSys() {
        return sys;
    }
    
    /**
     * Checks if sys is null or empty
     * @return True if empty or false if not 
     */
    public boolean isSysEmpty() {
        return (sys == null || sys.getBodies().isEmpty());
    }
    
    /**
     * Checks that both totalT and deltaT are grater than 0 
     * @return True if less than 0, else false
     */
    public boolean areTimesOk() {
        return (deltaT <= 0.0 || totalT <= 0.0);
    }
    
    /**
     * returns sys:s set of bodies to caller
     * @return ArrayList of systems bodies
     */
    public ArrayList<Body> getCurrBodies() {
        return sys.getBodies();
    }

    /**
     * Upon call loads sys from file using FileHandlers readFromFile 
     * @param inputFile File-object of input
     * @throws Exception if FileHandler reports some error
     */
    public void loadSys(File inputFile) throws Exception {
        sys = FileHandler.readFromFile(inputFile);
        steps = new ArrayList<>();
    }
    
    /**
     * Upon call loads steps from saved integration from file using
     * FileHandlers readIntFromFile
     * @param inputFile File-object of input
     * @throws Exception if FileHandler reports some error
     */
    public void loadSteps(File inputFile) throws Exception {
        steps = FileHandler.readIntFromFile(inputFile);
        sys = steps.get(steps.size() - 1);
    }
    
    public void saveSteps(File outputFile) throws Exception {
        FileHandler.writeIntToFile(steps, outputFile);
    }
    
    public IntegratorTask buildTask() {
        intTask = new IntegratorTask(totalT, deltaT, sys);
        return intTask;
    }
    
    public void postInt() throws ExecutionException, InterruptedException {
                        
        if (steps.size() > 0) {
            steps.remove(steps.size() - 1);
        }

        steps.addAll(intTask.get());        
    }
}
