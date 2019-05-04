
package gravitationalintegrator.domain;

import java.util.ArrayList;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SysTest {
    double delta = 0.000000001;
    Body body1;
    Body body2;
    Sys sys;
    
    @Before
    public void setUp() {  
        ArrayList<Body> bodies = new ArrayList<>();
        
        body1 = new Body(1.6, new ArrayRealVector(new double[]{0.75, 0.5, 1.0}), new ArrayRealVector(new double[]{0.01, 0.05, 0.1}));        
        body2 = new Body(1.6, new ArrayRealVector(new double[]{0.75, -0.5, -1.0}), new ArrayRealVector(new double[]{0.01, 0.05, 0.1}));        

        bodies.add(body1);
        bodies.add(body2);
                
        sys = new Sys(bodies);
    }
    

    //Test toString and coincidentaly constructor
    @Test
    public void testToString() {
        String expected = "0.0 1.6 0.75 0.5 1.0 0.01 0.05 0.1 1.6 0.75 -0.5 -1.0 0.01 0.05 0.1 \n";
        
        assertEquals(expected, sys.toString());
    }

    //Test setters and getters
    @Test
    public void testGetT() {
        double expected = 0.0;
        
        assertEquals(expected, sys.getT(), delta);
    }
    
    @Test
    public void testSetT() {
        sys.setT(1.0);
        
        double expected = 1.0;
        
        assertEquals(expected, sys.getT(), delta);
    }

    @Test
    public void testGetBodies1() {
        assertEquals(body1, sys.getBodies().get(0));
    }
    
    @Test
    public void testGetBodies2() {
        assertEquals(body2, sys.getBodies().get(1));
    }  
    
    @Test
    public void testSetBodies() {
        ArrayList<Body> bodies = new ArrayList<>();
        
        Body body = new Body(1.6, new ArrayRealVector(new double[]{0.0, 0.0, 0.0}), new ArrayRealVector(new double[]{0.0, 0.0, 0.0}));        

        bodies.add(body);
        bodies.add(body2);
        
        sys.setBodies(bodies);
        
        String expected = "0.0 1.6 0.0 0.0 0.0 0.0 0.0 0.0 1.6 0.75 -0.5 -1.0 0.01 0.05 0.1 \n";
        
        assertEquals(expected, sys.toString());
    }  
    
    @Test
    public void testGetCom() {
        RealVector com = new ArrayRealVector(new double[]{0.75, 0.0, 0.0});
        assertEquals(com.toString(), sys.getCom().toString());
    }
    
    @Test
    public void testUpdateCom() {
        ArrayList<Body> bodies = new ArrayList<>();
        
        Body body = new Body(1.6, new ArrayRealVector(new double[]{0.0, 0.0, 0.0}), new ArrayRealVector(new double[]{0.0, 0.0, 0.0}));        

        bodies.add(body);
        bodies.add(body2);
        
        //steBodies invokes updateCom, as do construcctor calls
        sys.setBodies(bodies);
        
        RealVector com = new ArrayRealVector(new double[]{0.375, -0.25, -0.5});
        
        assertEquals(com.toString(), sys.getCom().toString());
    }
    
    //Test constructor meant to copy another sys-object
    @Test
    public void testCloningConstructor() {
        Sys copySys = new Sys(sys);
        
        assertEquals(sys.toString(), copySys.toString());
    }
    
    //Check that clone is indeed new object
    public void testCloneNewObject() {
        Sys copySys = new Sys(sys);
        
        assertFalse(sys == copySys);
    }
}
