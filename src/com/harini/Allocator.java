package com.harini;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class Allocator {

    int rows, cols, totalSeatsAvailable, bufferSeats, bufferRows;
    Boolean[][] seats;
    int[] seatsInRow;
    LinkedHashMap<String, ArrayList<String>> reservedSeats;


    // constructor
    public Allocator() {
        this.rows = 10;
        this.cols = 20;
        this.totalSeatsAvailable = rows*cols;
        this.bufferSeats = 3;
        this.bufferRows = 1;
        this.seats = new Boolean[rows][cols];
        this.seatsInRow = new int[]{cols, cols, cols, cols, cols, cols, cols, cols, cols, cols};
        this.reservedSeats = new LinkedHashMap<>();

        for(int row=0; row<rows ; row++)
            Arrays.fill(seats[row],false);
    }

    /**
     * Process request and if number of seats requested is less than 20 assign seats is called, if not assign in
     * batches of 20 seats
     * @param reservationReq is a String with an ID, a space and then the number of seats needed
     * @return int 0 if request is valid, -2 for not enough seats, -1 if reservation requested is less than 0
     */
    public int reserveSeats(String reservationReq){
        String[] request = reservationReq.split(" ");
        int seatsRequested = Integer.parseInt(request[1]);
        String resId = request[0];

        if(seatsRequested <= 0) {
            return -1;
        }
        if(totalSeatsAvailable < seatsRequested){
            return -2;
        }

        if(seatsRequested > 20){
            int result = 0;
            while(seatsRequested > 20){
                result = assignSeats(resId, 20);
                seatsRequested -= 20;
            }
            return result;
        }
        return assignSeats(resId, seatsRequested);
    }

    /**
     * Checks if a single reservation request can be made to sit together or must be split up, then makes the allocation
     * @param resId the reservation identifier
     * @param seatsRequested requested number of seats in a reservation
     * @return int 0 if valid -1 if not enough seats(should have been caught already)
     */
    private int assignSeats(String resId, int seatsRequested) {
        int totalSeatsNeeded = seatsRequested;
        ArrayList<String> seatsList = new ArrayList<>();

        if(seatsRequested > totalSeatsAvailable) {
            return -1;
        }

        for (int rowIdx = rows - 1; rowIdx > 0; rowIdx--) {
            if (seatsInRow[rowIdx] >= seatsRequested) { // checking if they can sit together
                int[] availableSeats = allocateSeatsTogether(seatsRequested, rowIdx);
                if (availableSeats[0] != -1) {
                    int lastSeatAssigned;
                    lastSeatAssigned = availableSeats[1] + seatsRequested - 1;
                    Arrays.fill(seats[availableSeats[0]], availableSeats[1], lastSeatAssigned+1, true);

                    //Allocating buffer as specified
                    fillBuffer(availableSeats[0], availableSeats[1], seatsRequested, lastSeatAssigned);
                    seatsInRow[availableSeats[0]] -= seatsRequested;
                    totalSeatsAvailable -= seatsRequested;
                    if (!reservedSeats.containsKey(resId)) {
                        reservedSeats.put(resId, new ArrayList<>());
                    }
                    int count = 0;
                    while (count < totalSeatsNeeded) {
                        reservedSeats.get(resId).add((char) (availableSeats[0]+ 'A') + Integer.toString(count + availableSeats[1] + 1));
                        count++;
                    }
                    return 0;
                } else { //Splitting up seat allocation
                    int currRow = rows - 1;
                    while (totalSeatsNeeded > 0 && currRow >= 0) {
                        if (seatsInRow[currRow] > 1) {
                            for (int currCol = 0; currCol < cols - 1; currCol++) {
                                if (!seats[currRow][currCol]) {
                                    seats[currRow][currCol] = true;
                                    seatsList.add(currRow + " " + currCol);
                                    if (!reservedSeats.containsKey(resId)) {
                                        reservedSeats.put(resId, new ArrayList<>());
                                    }else {
                                        reservedSeats.get(resId).add((char) (currRow + 'A') + Integer.toString(currCol +1));
                                    }
                                    totalSeatsAvailable--;
                                    seatsInRow[currRow]--;
                                    totalSeatsNeeded--;
                                }
                            }
                        }
                        currRow--;
                    }
                    splitSeatsBuffer(seatsList);
                }
            }
        }
        return 0;
    }

    /**
     * Inorder to maximize customer satisfaction we have to check if we can seat a single reservation request together
     * @param seatsRequested is the requested number of seats from the reservation
     * @param rowIdx the first row that had enough seats in it
     * @return [row name, first seat number]
     */
    private int[] allocateSeatsTogether(int seatsRequested, int rowIdx) {
        int[] result = {-1,-1};

        for(int row = rowIdx; row >=0; row--){
            if(seatsInRow[row] > seatsRequested){
                int seatsToBeAllocated = seatsRequested;
                int seatCounter = 0;
                while(seatsToBeAllocated > 0 ){
                    if(seats[row][seatCounter]){
                        seatsToBeAllocated = seatsRequested;
                    }else{
                        seatsToBeAllocated--;
                    }
                    if(seatCounter!=cols-1) {
                        seatCounter++;
                    }else{
                        break;
                    }
                }
                if(seatsToBeAllocated == 0){
                    result[0]= row;
                    result[1]= seatCounter-seatsRequested;
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * The group of people in a single request are split and hence we can assume that the buffers can be set after they
     * are sitting so one can be in front of the other
     * @param seatsList seats that were allocated in different rows
     */
    private void splitSeatsBuffer(ArrayList<String> seatsList) {
        for (String seatBooked : seatsList) {
            String[] seat = seatBooked.split(" ");

            int row = Integer.parseInt(seat[0]);
            int col = Integer.parseInt(seat[1]);

            int rowCount = 1;
            while (rowCount < bufferRows && row - rowCount > rows) {
                int rowUp = row + rowCount;
                int rowBelow = row - rowCount;
                if (rowUp < rows-1 && !seats[rowUp][col] ) {
                    seats[rowUp][col] = true;
                }
                if (rowBelow > 0 && !seats[rowBelow][col] ) {
                    seats[rowBelow][col] = true;
                }
                rowCount++;
            }
            int colCount = 1;
            while (colCount < bufferSeats && col - colCount > cols) {
                int leftSeat = col + colCount;
                int rightSeat = col - colCount;
                if (rightSeat < cols && seats[row][rightSeat] == null) {
                    seats[row][rightSeat]= true;
                }
                if (leftSeat >= 0 && seats[row][leftSeat] == null) {
                    seats[row][leftSeat] = true;
                }
                colCount++;
            }
        }
    }

    /**
     * @param rowIdx  the row of the seats, goes by first seat
     * @param colIdx the first seat col in the booking
     * @param seatsRequested the total number of seats
     * @param lastSeat the last seat in the booking
     */
    private void fillBuffer(int rowIdx, int colIdx, int seatsRequested, int lastSeat) {
        //row buffer
        for (int numrows = 1; numrows <= bufferRows; numrows++) {
            if(rowIdx-numrows > 0) {
                for(int safety = 0; safety < seatsRequested; safety++) {
                    seats[rowIdx - numrows][colIdx+safety] = true;
                    seatsInRow[rowIdx - numrows] -= 1;
                    totalSeatsAvailable -= 1;
                }
            }
            if(numrows+rowIdx < rows-1){
                for(int safety = 0; safety < seatsRequested; safety++) {
                    seats[rowIdx + numrows][colIdx+safety] = true;
                    seatsInRow[rowIdx + numrows] -= 1;
                    totalSeatsAvailable -= 1;
                }
            }
        }

        for (int j =  1; j <= bufferSeats; j++) {
            if(j+lastSeat <= cols-1 ) {
                seats[rowIdx][lastSeat+j] = true;
                seatsInRow[rowIdx] -= 1;
                totalSeatsAvailable -=1;
            }
            if(colIdx-j>=0 && !seats[rowIdx][colIdx-j] ) {
                seats[rowIdx][colIdx-j] = true;
                seatsInRow[rowIdx] -= 1;
                totalSeatsAvailable -=1;
            }
        }
    }


    /**
     * this is called when we are done reading the file and then want to write to the file
     * @return our seating chart arrangement of valid reservation
     */
    public LinkedHashMap<String, ArrayList<String>> getReservationDetails() {
        return reservedSeats;
    }

}
