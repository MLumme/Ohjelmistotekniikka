
package gravitationalintegrator.domain;

import java.util.ArrayList;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class Integrator {
    double deltaT;
    Sys sys;
    
    public Integrator(double deltaT, Sys sys) {
        this.deltaT = deltaT;
        this.sys = sys;
    }
    
    public RealVector acceleration(Body body, int i) {
        RealVector acc = new ArrayRealVector(new double[]{0, 0, 0});
        ArrayList<Body> bodies = sys.getBodies();

        for (int j = 0; j < bodies.size(); j++) {
            if (i == j) {
                continue;
            }
            
            Body effector = bodies.get(j);
            
            RealVector distVec = body.getLoc().subtract(effector.getLoc());

            acc = acc.add(distVec.unitVector()
                    .mapMultiply(effector.getGm())
                    .mapDivide(Math.pow(distVec.getNorm(), 2.0)));
        }
        
        return acc;
    }
    
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
    
    public void updateFull(double totalT) {
        double currT = 0.0;
        while (currT < totalT) {
            this.updateOne();
            currT += deltaT; 
        }
    }
}
