
package com.mycompany.gravitationalintegrator;

import org.apache.commons.math3.linear.RealVector;

public class Body {
    final double GM;
    RealVector loc;
    RealVector vel;

    public Body(double GM, RealVector loc, RealVector vel) {
        this.GM = GM;
        this.loc = loc;
        this.vel = vel;
    }
    
    //constructor for copying class members
    public Body(Body toCopy) {
        this.GM = toCopy.getGM();
        this.loc = toCopy.getLoc();
        this.vel = toCopy.getVel();
    }

    public double getGM() {
        return GM;
    }

    public RealVector getLoc() {
        return loc;
    }

    public RealVector getVel() {
        return vel;
    }

    public void setLoc(RealVector loc) {
        this.loc = loc;
    }

    public void setVel(RealVector vel) {
        this.vel = vel;
    }

    @Override
    public String toString() {
        return GM + " " + loc.getEntry(0) + " " + loc.getEntry(1)
                + " " + loc.getEntry(2) + " " + vel.getEntry(0)
                + " " + vel.getEntry(1)+ " " + vel.getEntry(2);
    }
}
