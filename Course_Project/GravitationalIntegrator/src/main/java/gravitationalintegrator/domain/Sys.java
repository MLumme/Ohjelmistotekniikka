
package gravitationalintegrator.domain;

import java.util.ArrayList;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public final class Sys {
    double currentT;
    RealVector com;
    ArrayList<Body> bodies;

    public Sys(ArrayList<Body> bodies) {
        this.currentT = 0.0;
        this.bodies = bodies;
        this.com = this.updateCom();
    }
    
    public Sys(Sys toCopy) {
        bodies = new ArrayList<>();
        
        this.currentT = toCopy.getT();
        this.com = toCopy.getCom();
        for (Body body: toCopy.getBodies()) {
            this.bodies.add(new Body(body));
        }
        
        this.com = this.updateCom();
    }
    
    private RealVector updateCom() {
        RealVector updatedCom = new ArrayRealVector(new double[]{0.0, 0.0, 0.0});
        double systemGm = 0;
        
        for (Body body: bodies) {
            updatedCom = updatedCom.add(body.getLoc().mapMultiply(body.getGm()));
            systemGm += body.getGm();
        }
        
        updatedCom = updatedCom.mapDivide(systemGm);
        return updatedCom;
    }

    public ArrayList<Body> getBodies() {
        return bodies;
    }

    public void setBodies(ArrayList<Body> bodies) {
        this.bodies = bodies;
        this.com = this.updateCom();
    }
    
    public void recenter() {
        for (Body body: bodies) {
            
        }
    }
    
    public RealVector getCom() {
        return com;
    }

    public double getT() {
        return currentT;
    }

    public void setT(double t) {
        this.currentT = t;
    }

    @Override
    public String toString() {
        String output = this.currentT + "\n";
        for (Body body: this.bodies) {
            output += body.toString() + "\n";
        }
        
        return output;
    }
}
