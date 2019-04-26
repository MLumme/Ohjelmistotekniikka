
package gravitationalintegrator.domain;

import java.util.ArrayList;
import javafx.concurrent.Task;

/**
 * Extension of Task built to take in system, timestep and maximum time to run
 * integration on system until maximum time is reached
 */
public class IntegratorTask extends Task<ArrayList<Sys>> {
    private final double totalT;
    private final double deltaT;
    private final Sys sys;
    
    /**
     * Constructor which sets all parameters needed for integration
     * @param totalT Maximum time integrator will run
     * @param deltaT Timestep for integrator
     * @param sys System which to integrate
     */
    public IntegratorTask(double totalT, double deltaT, Sys sys) {
        this.totalT = totalT;
        this.deltaT = deltaT;
        this.sys = sys;
    }
    
    /**
     * Caller which runs the integrator once Thread-object in UI has time to call it
     * @return ArrayList containing copies of sys:s state at each timestep
     */
    @Override
    protected ArrayList<Sys> call() {
        double initT = sys.getT();
        double curT = sys.getT(); 
        double contT = curT + totalT;
        ArrayList<Sys> steps = new ArrayList<>();
        
        Integrator leapfrog = new Integrator(deltaT, sys);
        
        steps.add(new Sys(sys));
        
        while (curT < contT) {
            curT += deltaT;
            leapfrog.updateOne();
            steps.add(new Sys(sys));
            
            curT = sys.getT();
            updateProgress(curT - initT, contT - initT);            
        }
        
        return steps;
    }
    
}
