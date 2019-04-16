
package gravitationalintegrator.domain;

import java.util.ArrayList;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Markus
 */
public class IntegratorTest {
    Integrator leapfrog;
    Sys sys;
    double delta = 0.000000000001;    
    
    @Before
    public void setUp() {       
        Body body1 = new Body(1.0 , new ArrayRealVector(new double[]{0.0, 0.0, 0.0}), new ArrayRealVector(new double[]{2, 2, 1}));
        Body body2 = new Body(2.0 , new ArrayRealVector(new double[]{1.0, 0.0, 0.0}), new ArrayRealVector(new double[]{0, 0, 1}));
        Body body3 = new Body(3.0 , new ArrayRealVector(new double[]{0.0, 0.0, 1.0}), new ArrayRealVector(new double[]{1, 0, 1}));
        
        ArrayList<Body> bodies = new ArrayList<>();
        bodies.add(body1);
        bodies.add(body2);
        bodies.add(body3);
        
        sys = new Sys(bodies);
        leapfrog = new Integrator(1.0, sys);
    }
    
    //Test acceleration for two bodies
    @Test
    public void testAcceleration1() {
        Body body = sys.getBodies().get(0);
        
        RealVector acc = leapfrog.acceleration(body, 0);
        RealVector expected = new ArrayRealVector(new double[]{2.0, 0.0, 3.0});
        
        assertEquals(expected.toString(), acc.toString());
    }

    @Test
    public void testAcceleration2() {
        Body body = sys.getBodies().get(1);
        
        RealVector acc = leapfrog.acceleration(body, 1);
        RealVector expected = new ArrayRealVector(new double[]{-1.0 - 3.0/Math.pow(2.0, 1.5), 0.0, 3.0/Math.pow(2.0, 1.5)});
        
        assertEquals(expected.toString(), acc.toString());        
    }
    
    //Test that current time is correct after update
    @Test
    public void testUpdateOneUpdatesT() {
        leapfrog.updateOne();
        assertEquals(1.0, sys.getT(), delta);
    }
    
    //Test updateOne updates correct values to body, yes, this a horrible test
    @Test
    public void testUpdateOneUpdatesBody() {
        Body body = new Body(sys.getBodies().get(0));
        RealVector acc = leapfrog.acceleration(body, 0);
        body.setVel(body.getVel().add(acc.mapMultiply(0.5)));
        body.setLoc(body.getLoc().add(body.getVel().mapMultiply(1.0)));
        acc = leapfrog.acceleration(body, 0);
        body.setVel(body.getVel().add(acc.mapMultiply(0.5)));

        leapfrog.updateOne();
        
        assertEquals(body.toString(), sys.getBodies().get(0).toString());
    }
}
