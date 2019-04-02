
package com.mycompany.gravitationalintegrator;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class FileInput {
    public static Sys readFromFile(String file) {
        ArrayList<Body> bodies = new ArrayList<>();
        
        try{
            Scanner fileReader = new Scanner(new File(file));
            
            while(fileReader.hasNextLine()){
                String[] inputLine = fileReader.nextLine().split(" ");
                
                //stop if any input line in file not correct length
                if(inputLine.length != 7){
                    return null;
                }
                                
                Body body;
                
                //try to parse inputs from file as double precision values 
                try{
                    RealVector loc = new ArrayRealVector(
                            new double[]{Double.parseDouble(inputLine[1]),
                                Double.parseDouble(inputLine[2]),
                                Double.parseDouble(inputLine[3])});
                    
                    RealVector vel = new ArrayRealVector(
                            new double[]{Double.parseDouble(inputLine[4]),
                                Double.parseDouble(inputLine[5]),
                                Double.parseDouble(inputLine[6])});   
                    
                    body = new Body(Double.parseDouble(inputLine[0]), loc, vel);
                    
                }catch(NumberFormatException err){
                    return null;
                }
                
                bodies.add(body);
            }
            
            Sys sys = new Sys(bodies);
            return sys;
            
        }catch(FileNotFoundException err){
            return null;
        }
    }
}
