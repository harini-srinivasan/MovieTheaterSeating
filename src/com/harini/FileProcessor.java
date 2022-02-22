package com.harini;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileProcessor {


    /**
     * writing the reserved seats details for every request to the output file
     * @param reservedSeats is the generated seating allocation computed
     */
    public void writeToOutputFile(LinkedHashMap<String, ArrayList<String>> reservedSeats) {
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter("output.txt"));
            for (Map.Entry<String, ArrayList<String>> entry : reservedSeats.entrySet()) {
                String outputString = entry.getKey() + " " + entry.getValue();
                bufferedWriter.write(outputString + "\n");
            }
            bufferedWriter.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
