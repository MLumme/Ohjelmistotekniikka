
package gravitationalintegrator.domain;

import java.util.ArrayList;
import javafx.concurrent.Task;

public class IntegratorTask extends Task<ArrayList<Sys>> {
    private final double totalT;
    private final double deltaT;
    private final Sys sys;
    
    public IntegratorTask(double totalT, double deltaT, Sys sys) {
        this.totalT = totalT;
        this.deltaT = deltaT;
        this.sys = sys;
    }
    
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
