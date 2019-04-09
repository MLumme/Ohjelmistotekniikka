
package gravitationalintegrator.domain;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class BodyTest {
    Body body;
    double delta = 0.000000000001;
    
    @Before
    public void setUp() {
        body = new Body(1.6, new ArrayRealVector(new double[]{0.75, 0.5, 1.0}), new ArrayRealVector(new double[]{0.01, 0.05, 0.1}));        
    }
    
    //Test that body toString works 
    @Test
    public void testToString(){
        String expected = "1.6 0.75 0.5 1.0 0.01 0.05 0.1";
        assertEquals(expected, body.toString());
    }
    
    //Test that body copying works as intended via toString
    @Test
    public void testCloning() {
        Body copyBody = new Body(body);
        assertEquals(body.toString(), copyBody.toString());
    }
    
    @Test
    public void testBodyGetMass() {
        assertEquals(1.6, body.getGm(), delta);
    }
    
    @Test
    public void testBodyGetLoc() {
        RealVector loc = new ArrayRealVector(new double[]{0.75, 0.5, 1.0});
        assertEquals(loc, body.getLoc());
    }
    
    @Test
    public void testBodyGetVel() {
        RealVector vel = new ArrayRealVector(new double[]{0.01, 0.05, 0.1});
        assertEquals(vel, body.getVel());
    }
    
    @Test
    public void testBodySetLoc() {
        RealVector loc = new ArrayRealVector(new double[]{-0.75,0.5,-1.0});
        body.setLoc(loc);
        assertEquals(loc, body.getLoc());
    }
    
    @Test
    public void testBodySetVel() {
        RealVector vel = new ArrayRealVector(new double[]{0.01,-0.05,-0.1});
        body.setVel(vel);
        assertEquals(vel, body.getVel());
    }
}
