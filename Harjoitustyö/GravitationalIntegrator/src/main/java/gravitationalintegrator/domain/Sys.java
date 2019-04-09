
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
        this.currentT = toCopy.getT();
        this.com = toCopy.getCom();
        for (Body body: toCopy.getBodies()) {
            this.bodies.add(new Body(body));
        }
    }
    
    public RealVector updateCom() {
        RealVector updatedCOM = new ArrayRealVector(new double[]{0, 0, 0});
        double systemGM = 0;
        
        for (Body body: bodies) {
            updatedCOM.add(body.getLoc().mapMultiply(body.getGm()));
            systemGM += body.getGm();
        }
        
        updatedCOM.mapDivide(systemGM);

        return updatedCOM;
    }

    public ArrayList<Body> getBodies() {
        return bodies;
    }

    public void setBodies(ArrayList<Body> bodies) {
        this.bodies = bodies;
        this.com = this.updateCom();
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
        String output = "";
        for (Body body: this.bodies) {
            output += this.currentT + "\n" + body.toString() + "\n";
        }
        
        return output;
    }
}
