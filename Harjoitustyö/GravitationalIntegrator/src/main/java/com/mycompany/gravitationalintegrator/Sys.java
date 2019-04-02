
package com.mycompany.gravitationalintegrator;

import java.util.ArrayList;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public final class Sys {
    RealVector COM;
    ArrayList<Body> bodies;
    final int n;

    public Sys(ArrayList<Body> bodies) {
        this.bodies = bodies;
        this.n = bodies.size();
        this.COM = this.updateCOM();

    }
    
    public RealVector updateCOM() {
        RealVector updatedCOM = new ArrayRealVector(new double[]{0,0,0});
        double systemGM = 0;
        
        for(Body body: bodies){
            updatedCOM.add(body.getLoc().mapMultiply(body.getGM()));
            systemGM += body.getGM();
        }
        
        updatedCOM.mapDivide(systemGM);

        return updatedCOM;
    }

    public ArrayList<Body> getBodies() {
        return bodies;
    }

    public void setBodies(ArrayList<Body> bodies) {
        this.bodies = bodies;
    }
    
    public RealVector getCOM() {
        return COM;
    }

    @Override
    public String toString() {
        String output = "";
        for(Body body: this.bodies){
            output += body.toString() + "\n";
        }
        
        return output;
    }
}
