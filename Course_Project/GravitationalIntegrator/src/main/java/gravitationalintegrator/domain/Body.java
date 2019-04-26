
package gravitationalintegrator.domain;

import org.apache.commons.math3.linear.RealVector;
/**
 * Object for gravitational body
 */
public class Body {
    final double gm;
    RealVector loc;
    RealVector vel;

    /**
     * @param gm The gravitational parameter
     * @param loc RealVector for location
     * @param vel RealVector for velocity
     */
    public Body(double gm, RealVector loc, RealVector vel) {
        this.gm = gm;
        this.loc = loc;
        this.vel = vel;
    }
    
    /**
     * Constructor for creating copies of input body
     * @param toCopy Body to be copied
     */
    public Body(Body toCopy) {
        this.gm = toCopy.getGm();
        this.loc = toCopy.getLoc();
        this.vel = toCopy.getVel();
    }

    /**
     * Getter for gravitational parameter
     * @return Gravitational parameter
     */
    public double getGm() {
        return gm;
    }

    /**
     * Getter for location vector
     * @return Location RealVector
     */
    public RealVector getLoc() {
        return loc;
    }

    /**
     * Getter for velocity vector
     * @return Velocity RealVector
     */
    public RealVector getVel() {
        return vel;
    }

    /**
     * Set new location vector
     * @param loc Location RealVector
     */
    public void setLoc(RealVector loc) {
        this.loc = loc;
    }

    /**
     * Set new velocity vector
     * @param vel Velocity RealVector
     */
    public void setVel(RealVector vel) {
        this.vel = vel;
    }

    /**
     * Returns bodys parameters ordered as gm + loc + vel string
     * @return Parameters as string
     */
    @Override
    public String toString() {
        return gm + " " + loc.getEntry(0) + " " + loc.getEntry(1)
                + " " + loc.getEntry(2) + " " + vel.getEntry(0)
                + " " + vel.getEntry(1) + " " + vel.getEntry(2);
    }
}
