
package gravitationalintegrator.domain;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class FileHandler {
    public static Sys readFromFile(File inFile) throws Exception{
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
                    throw new Exception("Too few parameters, errror in line " + lineCounter + ", expected 7 parameters, was " + inputLine.length);
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
                    
                } catch (Exception err) {
                    throw new Exception("File reader unable to read input as double precision value, error in line: " + lineCounter);
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
    
    public static void writeToFile(ArrayList<Sys> steps, File outFile) throws IOException {
        try {
            FileWriter writer = new FileWriter(outFile, false);
            PrintWriter printer = new PrintWriter(writer);
            
            for (Sys sys: steps) {
                printer.print(sys.toString());
            }
            
            printer.close();
            writer.close();
            
        } catch (IOException err) {
            throw err;
        }
        
    }
}
