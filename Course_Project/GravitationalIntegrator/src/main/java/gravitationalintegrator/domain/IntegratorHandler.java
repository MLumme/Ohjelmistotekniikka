
package gravitationalintegrator.domain;

import gravitationalintegrator.io.FileHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class IntegratorHandler {
    private double deltaT;
    private double totalT;
    private Sys sys;
    private ArrayList<Sys> steps;
    private IntegratorTask intTask;

    public IntegratorHandler() {
        this.deltaT = 0.0;
        this.deltaT = 0.0;
        this.steps = new ArrayList<>();        
    }

    /**
     * Getter for totalT 
     * @return totalT
     */
    public double getTotalT() {
        return totalT;
    }

    /**
     * Getter for deltaT
     * @return deltaT
     */
    public double getDeltaT() {
        return deltaT;
    }
  
    /**
     * gets sys-objects currentT-value
     * @return sys.currentT or 0 if sys not initialized yet
     */
    public double getCurrT() {
        return sys == null ? 0.0 : sys.getT();
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
  
    /**
     * Returns count of integration timesteps steps
     * @return number of steps currently stored 
     */
    public int getStepCount() {
        return steps.size();
    }
    
    /**
     * For the purpose of testing
     * @param sys currently held Sys-object
     */
    public void setSys(Sys sys) {
        this.sys = sys;
    }
    
    /**
     * For the purpose of testing
     * @return sys
     */
    public Sys getSys() {
        return sys;
    }
    
    /**
     * Checks if sys is null or empty
     * @return True if empty or false if not 
     */
    public boolean isSysEmpty() {
        return (sys == null || sys.getBodies().size() < 2);
    }
    
    /**
     * Checks that both totalT and deltaT are greater than 0 
     * @return True if less than 0, else false
     */
    public boolean areTimesOk() {
        return (deltaT > 0.0 && totalT > 0.0);
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
     * Upon call loads steps from saved integration from file 
     * using FileHandlers readStepsFromFile
     * @param inputFile File-object of input
     * @throws Exception if FileHandler reports some error
     */
    public void loadSteps(File inputFile) throws Exception {
        steps = FileHandler.readStepsFromFile(inputFile);
        sys = steps.get(steps.size() - 1);
    }
    
    public void saveSteps(File outputFile) throws Exception {
        FileHandler.writeStepsToFile(steps, outputFile);
    }
    
    /**
     * Calls IntegratorTask() constructor, sets own intTask, passes on to caller
     * @return intTask
     */
    public IntegratorTask buildTask() {
        intTask = new IntegratorTask(totalT, deltaT, sys);
        return intTask;
    }
    
    /**
     * Allocates the thread for intTask to run in and starts it
     */
    public void startInt() {
        //Thread for running the integration
        Thread intThread = new Thread(intTask);
        intThread.setDaemon(true);
        intThread.start();        
    }
    
    
    /**
     * called on completion of integration, saves resulting integration steps
     * @throws ExecutionException if intTask throw one
     * @throws InterruptedException if intTask throw one
     */
    public void postInt() throws ExecutionException, InterruptedException {                    
        //If pre-existing integration results remove last to avoid duplication
        if (steps.size() > 0) {
            steps.remove(steps.size() - 1);
        }

        steps.addAll(intTask.get());        
    }
}
