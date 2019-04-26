
package gravitationalintegrator.domain;

import java.util.ArrayList;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Object for system and its bodies state at time currentT, is used in integration as bot 
 * continously updating state, and copied to keep record of system state during the integration
 */
public final class Sys {
    double currentT;
    RealVector com;
    ArrayList<Body> bodies;

    /**
     * Constructor building a new Sys-instance from a array of bodies, which initialises 
     * current time to 0.0 and computes system center of mass
     * @param bodies ArrayList containing system bodies
     */
    public Sys(ArrayList<Body> bodies) {
        this.currentT = 0.0;
        this.bodies = bodies;
        this.com = this.updateCom();
    }
    
    /**
     * Copy constructor to create new instance from old system
     * @param toCopy Old system to copy from
     */
    public Sys(Sys toCopy) {
        bodies = new ArrayList<>();
        
        this.currentT = toCopy.getT();
        this.com = toCopy.getCom();
        for (Body body: toCopy.getBodies()) {
            this.bodies.add(new Body(body));
        }
        
        this.com = this.updateCom();
    }
    
    /**
     * Helper to update center of mass for system 
     * @return updated center of mass
     */
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

    /**
     * Get the list of system bodies 
     * @return ArrayList of Body-objects
     */
    public ArrayList<Body> getBodies() {
        return bodies;
    }

    /**
     * Setter for replacing systems bodies with new batch
     * @param bodies ArrayList of replacement bodies 
     */
    public void setBodies(ArrayList<Body> bodies) {
        this.bodies = bodies;
        this.com = this.updateCom();
    }  
    
    /**
     * Getter for system center of mass
     * @return Center of mass RealVector
     */
    public RealVector getCom() {
        return com;
    }

    /**
     * Getter for systems current time
     * @return current time
     */
    public double getT() {
        return currentT;
    }

    /**
     * Setter for current time
     * @param t current time
     */
    public void setT(double t) {
        this.currentT = t;
    }

    /**
     * Tostring producing first current time and then mass and phase-space locations for bodies 
     * @return String representing system time and its bodies
     */
    @Override
    public String toString() {
        String output = this.currentT + "\n";
        for (Body body: this.bodies) {
            output += body.toString() + "\n";
        }
        
        return output;
    }
}
