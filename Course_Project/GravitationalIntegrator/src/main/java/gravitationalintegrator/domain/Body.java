
package gravitationalintegrator.domain;

import org.apache.commons.math3.linear.RealVector;

public class Body {
    final double gm;
    RealVector loc;
    RealVector vel;

    public Body(double gm, RealVector loc, RealVector vel) {
        this.gm = gm;
        this.loc = loc;
        this.vel = vel;
    }
    
    //constructor for copying class members
    public Body(Body toCopy) {
        this.gm = toCopy.getGm();
        this.loc = toCopy.getLoc();
        this.vel = toCopy.getVel();
    }

    public double getGm() {
        return gm;
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
        return gm + " " + loc.getEntry(0) + " " + loc.getEntry(1)
                + " " + loc.getEntry(2) + " " + vel.getEntry(0)
                + " " + vel.getEntry(1) + " " + vel.getEntry(2);
    }
}