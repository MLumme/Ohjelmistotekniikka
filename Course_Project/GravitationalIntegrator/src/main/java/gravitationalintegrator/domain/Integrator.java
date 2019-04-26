
package gravitationalintegrator.domain;

import java.util.ArrayList;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Class containing integrator using leapfrog-algorithm
 * @author Markus
 */
public class Integrator {
    double deltaT;
    Sys sys;
    
    /**
     * Constructor for Integrator class
     * @param deltaT Timestep used in integrator
     * @param sys System that will be integrated
     */
    public Integrator(double deltaT, Sys sys) {
        this.deltaT = deltaT;
        this.sys = sys;
    }
    
    /**
     * Computes acceleration caused on body by the rest of systems objects 
     * @param body Body being effected by system
     * @param i Index of body to avoid self-effects
     * @return Acceleration effecting body 
     */
    public RealVector acceleration(Body body, int i) {
        RealVector acc = new ArrayRealVector(new double[]{0, 0, 0});
        ArrayList<Body> bodies = sys.getBodies();

        for (int j = 0; j < bodies.size(); j++) {
            if (i == j) {
                continue;
            }
            
            Body effector = bodies.get(j);
            
            RealVector distVec = body.getLoc().subtract(effector.getLoc());
            
            //if distance of objects is zero then consider force zero to avoid 
            //singularity in division 
            if (distVec.getNorm() != 0) {
                acc = acc.subtract(distVec.unitVector()
                        .mapMultiply(effector.getGm())
                        .mapDivide(Math.pow(distVec.getNorm(), 2.0)));
            }
        }
        
        return acc;
    }
    
    /**
     * Integrate one timestep forward updating sys to new locations and velocities
     */
    public void updateOne() {
        ArrayList<Body> bodies = sys.getBodies();
        ArrayList<Body> newBodies = new ArrayList<>();
        
        for (int i = 0; i < bodies.size(); i++) {
            Body body = bodies.get(i);
            Body newBody = new Body(body);
            
            RealVector acc = acceleration(newBody, i);
            
            RealVector newLoc = body.getLoc();
            RealVector newVel = body.getVel();
            
            newVel = newVel.add(acc.mapMultiply(deltaT / 2.0));
            newLoc = newLoc.add(newVel.mapMultiply(deltaT));
            
            newBody.setLoc(newLoc);
            acc = acceleration(newBody, i);
            
            newVel = newVel.add(acc.mapMultiply(deltaT / 2.0));
            
            newBody.setVel(newVel);
            
            newBodies.add(newBody);
        }
        
        sys.setBodies(newBodies);
        sys.setT(sys.getT() + deltaT); 
    }
}
