/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gravitationalintegrator.domain;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class IntegratorTaskTest {
    IntegratorTask intTask;
    JFXPanel panel;
    Sys sys;
    Sys sys2;
    
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
        sys2 = new Sys(bodies);
        
        //together with tearDown() a horrible kludge from the depths of 
        //Stackowerflow to enable testing of JavaFX threads 
        panel = new JFXPanel();
    }  
    
    @After
    public void tearDown() {
        Platform.exit();
    }
    
    //Test that resulting array is of correct size
    @Test
    public void testIntegratorTaskSize() throws InterruptedException, ExecutionException, TimeoutException {    
        intTask = new IntegratorTask(10.0, 1.0, sys);
        
        Thread intThread = new Thread(intTask);
        intThread.setDaemon(true);
        intThread.start();

        
        ArrayList<Sys> steps = intTask.get(100, TimeUnit.MILLISECONDS);

        assertEquals(11, steps.size());
    }    
    
    //Test that integratorTask gives same result as running Integrator 
    //alone
    @Test
    public void testIntegratorTaskResult() throws InterruptedException, ExecutionException, TimeoutException {

        Integrator leapfrog = new Integrator(1.0,sys2);
        
        for (int i = 0; i < 5; i++) {
            leapfrog.updateOne();
        }
        
        //goes further than above on purpose
        intTask = new IntegratorTask(10.0, 1.0, sys);
        
        Thread intThread = new Thread(intTask);
        intThread.setDaemon(true);
        intThread.start();

        
        sys = intTask.get(100, TimeUnit.MILLISECONDS).get(5);
        
        assertEquals(sys2.toString(),sys.toString());
    }
}
