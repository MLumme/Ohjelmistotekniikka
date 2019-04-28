
package gravitationalintegrator.io;

import gravitationalintegrator.domain.Body;
import gravitationalintegrator.domain.Sys;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Class for file I/O operations
 */
public class FileHandler {
    /**
     * Method for reading a set of bodies on file into the program
     * @param inFile File-object containing the bodies
     * @return New Sys-object constructed from file contents 
     * @throws Exception in case of any of several different parsing-, 
     * file structure- or IO-error-handlers are tripped
     */
    public static Sys readFromFile(File inFile) throws Exception {
        ArrayList<Body> bodies = new ArrayList<>();
        
        try {
            Scanner fileReader = new Scanner(inFile);
            
            int lineCounter = 1;
            
            if (!fileReader.hasNextLine()) {
                throw new Exception("File empty");
            }
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String[] inputLine = line.split(" ");
                
                //stop if any input line in file not correct length
                if (inputLine.length != 7) {
                    fileReader.close();
                    throw errorThrower(1, lineCounter, 7, inputLine.length);
                }
                                
                Body body;
                
                //try to parse inputs from file as double precision values 
                try {
                    RealVector loc = new ArrayRealVector(
                            new double[]{Double.parseDouble(inputLine[1]),
                                Double.parseDouble(inputLine[2]),
                                Double.parseDouble(inputLine[3])});
                    
                    RealVector vel = new ArrayRealVector(
                            new double[]{Double.parseDouble(inputLine[4]),
                                Double.parseDouble(inputLine[5]),
                                Double.parseDouble(inputLine[6])});   
                    
                    body = new Body(Double.parseDouble(inputLine[0]), loc, vel);
                
                //catch errors in parsing strings to doubles
                } catch (Exception err) {
                    throw errorThrower(2, lineCounter, 0, 0);
                }
                
                bodies.add(body);
                lineCounter++;
            }
            
            fileReader.close();
            Sys sys = new Sys(bodies);
            return sys;
            
        } catch (FileNotFoundException err) {
            throw err;
        }
    }
    
    /**
     * Method for reading in some earlier integrations output to continue its integration
     * @param inFile File containing the output from some earlier integration
     * @return ArrayList of Sys-objects from some old integration run
     * @throws Exception in case of any of several different parsing-, 
     * file structure- or IO-error-handlers are tripped
     */
    public static ArrayList<Sys> readIntFromFile(File inFile) throws Exception {        
        try {
            Scanner fileReader = new Scanner(inFile);
            
            int lineCounter = 1;
            
            if (!fileReader.hasNextLine()) {
                throw new Exception("File empty");
            }
            
            String line = fileReader.nextLine();
            String[] inputLine = line.split(" ");
            
            //Check that first line contains number of integrated bodies and timesteps
            if (inputLine.length != 2) {
                throw errorThrower(1, lineCounter, 2, inputLine.length);            
            }
            
            int nSteps = 0;
            int nBodies = 0;
            
            //Attempt to parse nuber of bodies and steps
            try {
                nSteps = Integer.parseInt(inputLine[0]);
                nBodies = Integer.parseInt(inputLine[1]);
            } catch (Exception err) {
                throw errorThrower(2, lineCounter, 0, 0);
            }    
            
            ArrayList<Sys> steps = new ArrayList<>();
                        
            double time = 0.0;
            
            for (int i = 0; i < nSteps; i++) {
                //throw error if file ends before all bodies in
                if (!fileReader.hasNextLine()) {
                    fileReader.close();
                    throw errorThrower(1, 0, 0, 0);
                }
                
                line = fileReader.nextLine();
                inputLine = line.split(" ");
                
                //Test that row has correct number of parameters
                if (inputLine.length != 7*nBodies + 1) {
                    throw errorThrower(1, lineCounter, 7*nBodies + 1, inputLine.length); 
                }
                
                //Try to parse first column (time) as dounle
                try {
                    time = Double.parseDouble(inputLine[0]);
                } catch (NumberFormatException err) {
                    fileReader.close();
                    throw errorThrower(2, lineCounter, 0, 0);                
                }
                                
                ArrayList<Body> bodies = new ArrayList<>();
                
                //try to read rest of columns as body parameters 
                for (int j = 0; j < nBodies; j++) {
                    try {
                        Body body = bodyParser(Arrays.copyOfRange(inputLine, 1 + 7 * j, 8 * (1 + j)));
                        bodies.add(body);
                    } catch (NumberFormatException err) {
                        fileReader.close();
                        throw errorThrower(2, lineCounter, 0, 0);
                    } 
                }
       
                //initialize new system state, set its time to one read form file,
                //add to steps
                Sys sys = new Sys(bodies);
                sys.setT(time);
                
                steps.add(sys);    
                
                lineCounter++;
            }
            
            fileReader.close();
            return steps;
            
        } catch (FileNotFoundException err) {
            throw err;
        }    
    }
    
    /**
     * Method for writing steps from integration to file
     * @param steps ArrayList from integration
     * @param outFile File for output
     * @throws IOException exception if file cannot be opened or written in
     */
    public static void writeIntToFile(ArrayList<Sys> steps, File outFile) throws IOException {
        try {
            FileWriter writer = new FileWriter(outFile, false);
            PrintWriter printer = new PrintWriter(writer);
            
            printer.println(steps.size() + " " + steps.get(0).getBodies().size());
                        
            for (Sys sys: steps) {
                printer.print(sys.toString());
            }
            
            printer.close();
            writer.close();
            
        } catch (IOException err) {
            throw err;
        }
        
    }
    
    /**
     * Parses inputLine to convert its string elements to double and constructing 
     * Body-object from them
     * @param inputLine String-array with parameters for body-constructor
     * @return New Body-object
     * @throws Exception if input cant be converted to double-precision floats 
     */
    private static Body bodyParser(String[] inputLine) throws Exception {
        try {          
            RealVector loc = new ArrayRealVector(
                    new double[]{Double.parseDouble(inputLine[1]),
                    Double.parseDouble(inputLine[2]),
                    Double.parseDouble(inputLine[3])});

            RealVector vel = new ArrayRealVector(
                new double[]{Double.parseDouble(inputLine[4]),
                    Double.parseDouble(inputLine[5]),
                    Double.parseDouble(inputLine[6])});   

            Body body = new Body(Double.parseDouble(inputLine[0]), loc, vel);

            return body;
        } catch (NumberFormatException err) {
            throw err;
        }   
    }
    
    /**
     * Exception constructor for set of 3 repeating error types
     * @param type Type of error, from 1 to 3
     * @param lineCounter current line in which error was detected
     * @param expectedCount if applicable, expected number of parameters
     * @param paramCount if applicable, real number of parameters
     * @return Exception with one of the repeating messages
     */
    private static Exception errorThrower(int type, int lineCounter, int expectedCount, int paramCount) {
        switch (type) {
            case 1:
                return new Exception("Wrong number of parameters, error in line " 
                        + lineCounter + ", expected " + expectedCount + " parameters, found " + paramCount);
            case 2:
                return new Exception("Unable to parse to correct number format, "
                        + "error in line " + lineCounter);
            case 3: 
                return new Exception("EOF reached sooner than expected");
        }
        
        return null;
    }    
        
}
