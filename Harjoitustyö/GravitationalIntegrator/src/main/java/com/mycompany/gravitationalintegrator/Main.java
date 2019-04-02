
package com.mycompany.gravitationalintegrator;

import java.util.Scanner;
import org.apache.commons.math3.linear.ArrayRealVector;

public class Main {

    public static void main(String[] args) {
        
        //temporary UI until graphical one is done
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input filename for file with parameters for bodies:");
        String inputFile = scanner.nextLine();
        
        System.out.println("Timestep in seconds:");
        Double deltaT = Double.parseDouble(scanner.nextLine());
        
        System.out.println("Total simulation time in seconds:");
        Double totalT = Double.parseDouble(scanner.nextLine());

        Sys sys = FileInput.readFromFile(inputFile);
        
        if(sys == null){
            System.out.println("Error in input file");
            return;
        }
        Integrator leapfrog = new Integrator(deltaT,sys);
        
        System.out.println("Initial parameters for input bodies:");
        System.out.println(sys.toString());
        leapfrog.updateFull(totalT);
        
        System.out.println("Final parameters for input bodies:");
        System.out.println(sys.toString());
    }
    
}
