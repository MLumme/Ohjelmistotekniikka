
package gravitationalintegrator.domain;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntegratorHandlerTest {
    IntegratorHandler intHandler; 
    double delta = 1E-9;
    Body body1;
    Body body2;
    Sys sys;
    JFXPanel panel;
    
    @Before
    public void setUp() {
        intHandler = new IntegratorHandler();
        
        ArrayList<Body> bodies = new ArrayList<>();
        
        body1 = new Body(1.6, new ArrayRealVector(new double[]{0.75, 0.5, 1.0}), new ArrayRealVector(new double[]{0.01, 0.05, 0.1}));        
        body2 = new Body(1.6, new ArrayRealVector(new double[]{0.75, -0.5, -1.0}), new ArrayRealVector(new double[]{0.01, 0.05, 0.1}));        

        bodies.add(body1);
        bodies.add(body2);
                
        sys = new Sys(bodies);
        sys.setT(1.0);
        
        intHandler.setSys(sys);
        
        //together with tearDown() a horrible kludge from the depths of
        //Stackowerflow to enable testing of JavaFX threads
        panel = new JFXPanel();
    }
    
    @After
    public void tearDown() {
        Platform.exit();
    }
    
    //test that getSys and getSys work to validate using setUp
    @Test
    public void testSetAndGetSys() {
        ArrayList<Body> bodies = new ArrayList<>(); 
    
        bodies.add(new Body(1.0, new ArrayRealVector(new double[]{0.0, 0.0, 0.0}), new ArrayRealVector(new double[]{0.0, 0.00, 0.0})));
        bodies.add(new Body(1.0, new ArrayRealVector(new double[]{1.0, 0.0, 1.0}), new ArrayRealVector(new double[]{1.0, 0.00, 1.0})));
        
        Sys locSys = new Sys(bodies);
        
        intHandler.setSys(locSys);
        
        Sys compSys = intHandler.getSys();
        
        boolean isEqual = false;
        
        if (compSys.getT() == 0.0 &&
                compSys.getBodies().get(1).toString()
                .equals("1.0 1.0 0.0 1.0 1.0 0.0 1.0")) {
           isEqual = true; 
        }
            
        assertTrue(isEqual);
    }
    
    //Test rest of Get/Set pairs
    @Test
    public void testGetCurrTWhenSysExists() {
        assertEquals(1.0, intHandler.getCurrT(), delta);
    }
    
    @Test
    public void testGetCurrTWhenSysNotExists() {
        IntegratorHandler tempHandler = new IntegratorHandler();
        assertEquals(0.0, tempHandler.getCurrT(), delta);
    }
    
    @Test
    public void testSetGetTs() {
        intHandler.setTs(1E6, 1E3);
        double totalT = intHandler.getTotalT();
        double deltaT = intHandler.getDeltaT();
        
        boolean isEqual = false;
        
        if (totalT == 1E6 && deltaT == 1E3) {
            isEqual = true;
        }
        
        assertTrue(isEqual);
    }
    
    //test that current list of bodies is returned
    @Test
    public void testGetCurrBodies() {
        assertEquals(body2.toString(), intHandler.getCurrBodies().get(1).toString());
    }
    
    //test if number of bodies null or too small
    @Test
    public void testNullSys() {
        intHandler.setSys(null);
        
        assertTrue(intHandler.isSysEmpty());
    }
    
    @Test
    public void testLT2Bodies() {
        ArrayList<Body> bodies = new ArrayList<>();
        bodies.add(body1);
        intHandler.getSys().setBodies(bodies);
        
        assertTrue(intHandler.isSysEmpty());
    }
    
    @Test
    public void test2bodies() {
        assertFalse(intHandler.isSysEmpty());
    }
    
    //Test that times are evaluated as correct
    @Test
    public void testTimeNotOk() {
        assertFalse(intHandler.areTimesOk());
    }
    
    @Test 
    public void testTimeOk() {
        intHandler.setTs(1E3, 1E0);
        
        assertTrue(intHandler.areTimesOk());
    }
    @Test 
    public void testTimeOkFirsZero() {
        intHandler.setTs(0.0, 1E3);
        
        assertFalse(intHandler.areTimesOk());
    }  
    
    @Test 
    public void testTimeOkLastZero() {
        intHandler.setTs(1E3, 0.0);
        
        assertFalse(intHandler.areTimesOk());
    }
    
    //test step count returning method
    @Test
    public void testStepsSizeAtBeginning() {
        int count = intHandler.getStepCount();
        
        assertEquals(0, count);
    }
    
    //test that intHandlers task- and thread-methods result in correct sized steps-array
    @Test
    public void testIntegratorHandlerTaskControlsSize() throws InterruptedException, ExecutionException, TimeoutException {    
        intHandler.setTs(10.0, 1.0);
        IntegratorTask intTask = intHandler.buildTask();
        intHandler.startInt();
        
        //forces test to wait untill computation hopefully over, continues
        //to post-integration operations
        intTask.get(100, TimeUnit.MILLISECONDS);

        intHandler.postInt();

        //Second runt to test that continuing produces correct size
        intTask = intHandler.buildTask();
        intHandler.startInt();
        
        intTask.get(100, TimeUnit.MILLISECONDS);
        
        intHandler.postInt();
        
        assertEquals(21, intHandler.getStepCount());
    }    
    
    //test that intHandlers task- and threading-methods provide expected results 
    //from integration when compared with Integrator alone
    @Test
    public void testIntegratorHandlerTaskControlsResults() throws InterruptedException, ExecutionException, TimeoutException {    
        Sys compSys = new Sys(sys);
        
        Integrator leapfrog = new Integrator(1.0,compSys);
        
        for (int i = 0; i < 10; i++) {
            leapfrog.updateOne();
        }
        
        intHandler.setTs(10.0, 1.0);
        IntegratorTask intTask = intHandler.buildTask();
        intHandler.startInt();
        
        //forces program to wait untill vomputation hopefully over, continues
        //to post-integration operations
        intTask.get(100, TimeUnit.MILLISECONDS);

        intHandler.postInt();
        
        assertEquals(compSys.toString(), sys.toString());
    }      
}
