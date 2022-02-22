package com.harini;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        if(args.length==0){
            System.out.println("Input file path not given!");
            System.exit(1);
        }

        FileProcessor fileProcessor = new FileProcessor();
        Allocator allocator = new Allocator();

        try {
            File file = new File(args[0]);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();

            while (line != null) {
                int output = allocator.reserveSeats(line);
                if (output == -1) {
                    System.out.println("Number of seats requested not valid "+ line);
                }
                if (output == -2) {
                    System.out .println("Sorry, there are not sufficient seats available to process your request " + line);
                }
                line = bufferedReader.readLine();
            }

            fileProcessor.writeToOutputFile(allocator.getReservationDetails());

        } catch (FileNotFoundException ex) {
            System.err.println("Input file does not exist in given path");
            ex.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
