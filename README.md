# Movie Theater Seating

## Problem Statement
Implement an algorithm for assigning seats within a movie theater to fulfill reservation requests given in input file. Movie theater has a seating arrangement of 10 rows x 20 seats. The goal is to design and write a seat assignment program to maximize both customer satisfaction and customer safety. 

NOTE: For the purpose of public safety, assume that a buffer of three seats and/or one row is required.

## Steps to execute program and tests
* Jdk 17+ Java access from command prompt(set java Path variable). 
* Clone/Download project from repository and go to src folder in the project. 
* To compile - javac <package_name>\Main.java
* To execute program - java <package_name>.Main <input_file_path>
* Program will print output file path and/or messages for each request.
* To compile tests - 

## Assumptions
* Solution designed is for the given theatre layout of 10 rows with 20 seats each.
* Reservation requests processed in the order specified in the input file, first come first serve basis.
* Reservation request can either be accomodated(total seats available is greater than or equal to the requested number) or show not possible to accomodate message. No waitlisting requests.
* Buffer row of 1 is only for the corresponding seat(s) in rows above and below the current reseravtion. 
* Buffer rows and columns should be created for every request that is accomodated.

## Solution 
* Starting to fulfill requests from row A(10th row) filling seats from 0 to 20.
* First, try to seat all people in a single reservation request together(next to each other) in a single row to ensure **customer satisfaction**. If not possible(either when more than 20 seats requested or when single row vacant seats is lesser than requested number), then seat them in different rows.
* Update available seats count for every accomodation and keep track of the exact seats given to the request(mark as unavailable for further requests).
* Allocate buffer for 1 row before and after the allocated row, and also 3 seats to the left and right of allocated seats in a row. This accounts for **customer safety**.
* For requests that cannot be fulfilled, print appropriate message, for fulfilled requests, print/write the seats allocated in the output file. Return results.

## Scope for improvement
To write many more test cases to ensure all edge cases are checked and covered using junit tests. 
