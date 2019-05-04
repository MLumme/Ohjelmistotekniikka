/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gravitationalintegrator.io;

import gravitationalintegrator.domain.Body;
import gravitationalintegrator.domain.Sys;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author Markus
 */
public class FileHandlerTest {
    ArrayList<Sys> steps;
    
    Sys sys1;
    Sys sys2;
    
    Body body1;
    Body body2;
    Body body3;
    Body body4;
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Before
    public void setUp() throws IOException {
        steps = new ArrayList<>();
        
        body1 = new Body(1.0, new ArrayRealVector(new double[]{0.0, 0.0, 0.0}), new ArrayRealVector(new double[]{1.0, 1.0, 0.0}));
        body2 = new Body(2.0, new ArrayRealVector(new double[]{0.0, 0.0, 0.0}), new ArrayRealVector(new double[]{0.0, 0.0, 0.0}));
        body3 = new Body(1.0, new ArrayRealVector(new double[]{1.0, 1.0, 0.0}), new ArrayRealVector(new double[]{2.0, 2.0, 0.0}));
        body4 = new Body(2.0, new ArrayRealVector(new double[]{0.5, 0.0, 0.5}), new ArrayRealVector(new double[]{1.0, 0.0, 1.0}));
  
        ArrayList<Body> bodies = new ArrayList<>();
        
        bodies.add(body1);
        bodies.add(body2);

        sys1 = new Sys(bodies);
        steps.add(new Sys(sys1));
        
        bodies.clear();
        bodies.add(body3);
        bodies.add(body4);
        
        sys2 = new Sys(bodies);
        sys2.setT(1.0);
        
        steps.add(sys2);
    }
    
    //Test initial params for nonexistant file
    @Test(expected = Exception.class)
    public void testBodyNonexistant() throws Exception {
        //initalize a file with incorrect path and path to folder to force an 
        //IOException
        File temp = folder.newFile("F16");
        File pathToFolder = folder.newFolder();
        File nonexistingPath = new File(temp.getAbsolutePath());
        temp.delete();
        
        FileHandler.readFromFile(nonexistingPath);
    }
    
    //Test initial params for empty file
    @Test(expected = Exception.class)
    public void testBodyFileEmpty() throws Exception {
        //Initialize an empty parameter file
        File bodyFileEmpty = folder.newFile("F0");
         
        FileHandler.readFromFile(bodyFileEmpty);     
    }
    
    //Test initial parameters in case of too few bodies 
    @Test(expected = Exception.class)
    public void testBodyFileTooFewBodies() throws Exception {
        //Initalize an file with too few bodies
        File bodyTooFewBodies = folder.newFile("F2");
        
        FileWriter writer = new FileWriter(bodyTooFewBodies, false);
        PrintWriter printer = new PrintWriter(writer);
        
        printer.println(body1.toString());
        
        printer.close();
        writer.close();  
        
        FileHandler.readFromFile(bodyTooFewBodies);
    }
    
    //Test initial parameters for too few parameters on one line
    @Test(expected = Exception.class)
    public void testBodyFileTooFewParams() throws Exception {
        //Initialize an file with too few parameters on one row
        File bodyTooFewParams = folder.newFile("F3");
        
        FileWriter writer = new FileWriter(bodyTooFewParams, false);
        PrintWriter printer = new PrintWriter(writer);
        
        printer.println(body1.toString()); 
        printer.println("1.0 1.0 1.0");

        printer.close();
        writer.close();  
        
        FileHandler.readFromFile(bodyTooFewParams);
    }   
    
    //Test initial parameters for unparseable input
    @Test(expected = Exception.class)
    public void testBodyFileUnparseable() throws Exception {
        //Initialize an file with letters in place of doubles
        File bodyUnableToParse = folder.newFile("F4");
        
        FileWriter writer = new FileWriter(bodyUnableToParse, false);
        PrintWriter printer = new PrintWriter(writer);
        
        printer.println(body1.toString()); 
        printer.println("1.0 1.0 1.0 1.0 error 1.0 1.0");

        printer.close();
        writer.close();  
        
        FileHandler.readFromFile(bodyUnableToParse);
    }  
    
    //Test inital parameters for successful read
    @Test
    public void testBodySuccessfulRead() throws Exception {
        //Initialize perfectly normal file
        File bodyWorkingFile = folder.newFile("F5");
        
        FileWriter writer = new FileWriter(bodyWorkingFile, false);
        PrintWriter printer = new PrintWriter(writer);
        
        printer.println(body1.toString()); 
        printer.println(body2.toString());

        printer.close();
        writer.close();
        
        Sys sys = FileHandler.readFromFile(bodyWorkingFile);
        ArrayList<Body> bodies = new ArrayList<>();
        bodies.add(body1);
        bodies.add(body2);
        
        Sys compSys = new Sys(bodies);
        
        assertEquals(compSys.toString(), sys.toString());
    }

    //Test steps loader for nonexistant file
    @Test(expected = Exception.class)
    public void testStepsNonexistant() throws Exception {
        //initalize a file with incorrect path and path to folder to force an 
        //IOException
        File temp = folder.newFile("F16");
        File pathToFolder = folder.newFolder();
        File nonexistingPath = new File(temp.getAbsolutePath());
        temp.delete();
        
        FileHandler.readStepsFromFile(nonexistingPath);
    }
    
    //Test that empty steps file throws exception
    @Test(expected = Exception.class)
    public void stepsEmptyFile() throws Exception {
        //initialize steps file empty
        File stepsFileEmpty = folder.newFile("F6");
        
        FileHandler.readStepsFromFile(stepsFileEmpty);
    }
    
    //Test steps file with wrong number of simulation parameters
    @Test(expected = Exception.class)
    public void stepsWrongIntParamAmount() throws Exception {
        //initalize steps file with incorrect number of file params
        File stepsWrongStart = folder.newFile("F7");
        
        FileWriter writer = new FileWriter(stepsWrongStart, false);
        PrintWriter printer = new PrintWriter(writer); 
        
        printer.println("2");
        printer.print(sys1.toString());
        printer.print(sys2.toString());
        
        printer.close();
        writer.close();
        
        FileHandler.readStepsFromFile(stepsWrongStart);
    }
    
    //Tests steps file for unparseable firs row member
    @Test(expected = Exception.class)
    public void stepsUnparseableIntParam() throws Exception {
        //initalize steps file with unparseable first line
        File stepsUnparseableStart = folder.newFile("F8");
        
        FileWriter writer = new FileWriter(stepsUnparseableStart, false);
        PrintWriter printer = new PrintWriter(writer); 
        
        printer.println("2 ERROR");
        printer.print(sys1.toString());
        printer.print(sys2.toString());   
 
        printer.close();
        writer.close();
        
        FileHandler.readStepsFromFile(stepsUnparseableStart);
    }
    
    //Tests that incorrect number of parameters causes error
    @Test(expected = Exception.class)
    public void stepsWrongNumberofSysParams() throws Exception {
        //initalize steps file for wrong number of parameters in system
        File stepsWrongNumberOfParams = folder.newFile("F9");
        
        FileWriter writer = new FileWriter(stepsWrongNumberOfParams, false);
        PrintWriter printer = new PrintWriter(writer); 
        
        printer.println("2 2");
        printer.print("0.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0 \n");
        printer.print(sys2.toString());
        
        printer.close();
        writer.close();
        
        FileHandler.readStepsFromFile(stepsWrongNumberOfParams);
    }  
    
    //Tests that unparseable parameters causes error
    @Test(expected = Exception.class)
    public void stepsUnparseableSysParam() throws Exception {
        //initalize steps file for unparseable parameters
        File stepsUnparseableParams = folder.newFile("F10");
        
        FileWriter writer = new FileWriter(stepsUnparseableParams, false);
        PrintWriter printer = new PrintWriter(writer); 
        
        printer.println("2 2");
        printer.print("0.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0 ERROR 1.0 1.0 1.0 1.0 1.0 1.0 \n");
        printer.print(sys2.toString());
        
        printer.close();
        writer.close();  
        
        FileHandler.readStepsFromFile(stepsUnparseableParams);
    }
    
    //Tests that less than expected number of lines throws error
    @Test(expected = Exception.class)
    public void stepsTooFewLines() throws Exception {
        //initalize steps file with less timesteps than expected
        File stepsTooFewSteps = folder.newFile("F11");
        
        FileWriter writer = new FileWriter(stepsTooFewSteps, false);
        PrintWriter printer = new PrintWriter(writer); 
        
        printer.println("3 2");
        printer.print(sys1.toString());
        printer.print(sys2.toString());
        
        printer.close();
        writer.close(); 
        
        FileHandler.readStepsFromFile(stepsTooFewSteps);
    }

    //Tests that working file reads normally
    @Test
    public void stepsTestRead() throws Exception {
        //initalize working file
        File stepsWorkingFile = folder.newFile("F12");
        
        FileWriter writer = new FileWriter(stepsWorkingFile, false);
        PrintWriter printer = new PrintWriter(writer); 
        
        printer.println("2 2");
        printer.print(sys2.toString());
        printer.print(sys2.toString());
        
        printer.close();
        writer.close(); 
        
        steps = FileHandler.readStepsFromFile(stepsWorkingFile);
        
        assertEquals(sys2.toString(), steps.get(1).toString());
    }
    
    //Tests that zero timesteps causes error
    @Test(expected = Exception.class)
    public void stepsTooFewLinesInStart() throws Exception {
        //initialize file with too few timesteps
        File stepsZeroSteps = folder.newFile("F13");
        
        FileWriter writer = new FileWriter(stepsZeroSteps, false);
        PrintWriter printer = new PrintWriter(writer); 
        
        printer.println("0 2");
        
        printer.close();
        writer.close();
        
        FileHandler.readStepsFromFile(stepsZeroSteps);
    }    
    
    //Tests that zero timesteps causes error
    @Test(expected = Exception.class)
    public void stepsTooFewBodiesInStart() throws Exception {
        //initalize steps with zero bodies given
        File stepsZeroBodies = folder.newFile("F14");
        
        FileWriter writer = new FileWriter(stepsZeroBodies, false);
        PrintWriter printer = new PrintWriter(writer); 
        
        printer.println("2 1");
        
        printer.close();
        writer.close(); 
        
        FileHandler.readStepsFromFile(stepsZeroBodies);
    } 
    
    //Tests that unparseable time causes error
    @Test(expected = Exception.class)
    public void stepsUnparseableTime() throws Exception {
        //initalize steps with unparseable time in one row
        File stepsUnparseableTime = folder.newFile("F15");
        
        FileWriter writer = new FileWriter(stepsUnparseableTime, false);
        PrintWriter printer = new PrintWriter(writer); 
        
        printer.println("2 2");
        printer.print("ERROR 1.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0 \n");
        printer.print(sys2.toString());
        
        printer.close();
        writer.close();  
        
        FileHandler.readStepsFromFile(stepsUnparseableTime);
    }     
    
    //Test writer for nonexistant file
    @Test(expected = IOException.class)
    public void testWriterIOException() throws IOException {
        //initalize a file with incorrect path and path to folder to force an 
        //IOException
        File temp = folder.newFile("F16");
        File pathToFolder = folder.newFolder();
        File nonexistingPath = new File(temp.getAbsolutePath());
        temp.delete();
        
        FileHandler.writeStepsToFile(steps, pathToFolder);
    }
    
    //Test that writer works
    @Test
    public void stepsWriterWorks() throws Exception {
        //initialize steps file empty
        File stepsFileEmpty = folder.newFile("F6");
        
        FileHandler.writeStepsToFile(steps, stepsFileEmpty);
        steps = FileHandler.readStepsFromFile(stepsFileEmpty);
        
        assertEquals(sys2.toString(), steps.get(1).toString());
    }
}
