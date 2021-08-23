/**
 * Project: aTeam Final Project 
 * Description: Program for reading milk weight inputs and displaying data tables of milk categories 
 * Authors: Jeffrey Li, Albert Men, Moulik Mehta, Yash Hindka 
 * Email: jkli@wisc.edu, awmen@wisc.edu, moulik.mehta@wisc.edu, yhindka@wisc.edu
 * 
 * Course: CS400 Semester: Spring 2020 Lecture: LEC001
 */

package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;

/**
 * This class represents a FarmDataManager data structure. It stores all of the back-end data.
 * 
 * @author everyone
 */
public class FarmDataManager {

  private List<Year> years; // list of years

  /**
   * Constructor, creates new instance of FarmDataManger.
   **/
  public FarmDataManager() {
    years = new ArrayList<Year>();
  }

  /**
   * Gets the total milk weight for a month.
   * 
   * @author Yash Hindka
   * 
   * @param month - to get milk weight for
   * @param year  - year that the month resides in
   * @return total milk weight for month.
   */
  public int getTotalMilkWeightMonth(int month, int year) {

    Month specified = null;
    try {
      specified = getSpecifiedYear(year).getMonth(month);
    } catch (NullPointerException e) {
      return 0; // year does not exist, so sum must be 0
    }
    if (specified == null) {
      return 0; // month does not exist, so sum must be 0
    }
    return specified.getTotalMilkWeightMonth();

  }

  /**
   * Retrieves total milk weight for all farms for specified year.
   *
   * @author Yash Hindka
   *
   * @param year - # year to get weight for
   *
   * @return total milk weight for year
   **/
  public int getTotalMilkWeightYear(int year) {

    Year specified = getSpecifiedYear(year);
    return specified.getTotalMilkWeightYear();
  }

  /**
   * Gets total Milk weight for a specific farm for a date range.
   * 
   * @author Yash Hindka, Jeffrey Li
   * 
   * @param farmID     - to get milk weight for
   * @param startYear  - year to begin sum
   * @param startMonth - month to begin sum
   * @param startDay   - day to begin sum
   * @param endYear    - year to end sum
   * @param endMonth   - month to end sum
   * @param endDay     - day to end sum
   * @return total milk weight
   */
  public int getDateRangeMilkWeightFarm(String farmID, int startYear, int startMonth, int startDay,
      int endYear, int endMonth, int endDay) {

    int sum = 0;

    for (int i = startYear; i <= endYear; i++) {
      // need to check if date range starts and ends in same year
      if (i == startYear && i == endYear) {
        // if so, then sum from specified day in startMonth through specified day in endMonth
        for (int j = startMonth; j <= endMonth; j++) {
          if (j == startMonth && j == endMonth) {
            try {
              sum += getSpecifiedYear(i).getMonth(j - 1).getTotalMilkWeightDateRangeMonth(startDay,
                  endDay, farmID);
            } catch (FarmNotFoundException e) {

            } catch (NullPointerException e) {

            }
          } else if (j == startMonth) {
            try {
              sum += getSpecifiedYear(i).getMonth(j - 1).getTotalMilkWeightDateRangeMonth(startDay,
                  getSpecifiedYear(i).getMonth(j - 1).calculateMonthSize(), farmID);
            } catch (FarmNotFoundException e) {

            } catch (NullPointerException e) {

            }
          } else if (j == endMonth) {
            try {
              sum += getSpecifiedYear(i).getMonth(j - 1).getTotalMilkWeightDateRangeMonth(1, endDay,
                  farmID);
            } catch (FarmNotFoundException e) {

            } catch (NullPointerException e) {

            }
          } else {
            try {
              sum += getMonthWeightForFarm(farmID, j - 1, i);
            } catch (NullPointerException e) {

            }
          }
        }
      }
      // in first iteration, need to get sum from startMonth through December of startYear
      else if (i == startYear) {
        for (int j = startMonth; j <= 12; j++) {
          if (j == startMonth) {
            try {
              sum += getSpecifiedYear(i).getMonth(j - 1).getTotalMilkWeightDateRangeMonth(startDay,
                  getSpecifiedYear(i).getMonth(j - 1).calculateMonthSize(), farmID);
            } catch (FarmNotFoundException e) {

            } catch (NullPointerException e) {

            }
          } else {
            try {
              sum += getMonthWeightForFarm(farmID, j - 1, i);
            } catch (NullPointerException e) {

            }
          }
        }
      }
      // in last iteration, need to get sum from January through endMonth of endYear
      else if (i == endYear) {
        for (int j = 1; j <= endMonth; j++) {
          if (j == endMonth) {
            try {
              sum += getSpecifiedYear(i).getMonth(j - 1).getTotalMilkWeightDateRangeMonth(1, endDay,
                  farmID);
            } catch (FarmNotFoundException e) {

            } catch (NullPointerException e) {

            }
          } else {
            try {
              sum += getMonthWeightForFarm(farmID, j - 1, i);
            } catch (NullPointerException e) {

            }
          }
        }
      }
      // in any other iteration, need to get sum from January to December of the current year being
      // traversed
      else {
        for (int j = 1; j <= 12; j++) {
          try {
            sum += getMonthWeightForFarm(farmID, j - 1, i);
          } catch (NullPointerException e) {

          }
        }
      }
    }

    return sum;
  }

  /**
   * Helper method to retrieve instance of specified Year.
   *
   * @author Yash Hindka
   *
   * @param year - # year to get instance of
   * @return Year object associated with specified year.
   **/
  public Year getSpecifiedYear(int year) {
    Year specified = null;
    for (Year check : years) {
      if (check.getYearNumber() == year) {
        specified = check;
        break;
      }
    }
    return specified;
  }

  /**
   * Method used to get the total weight of a specfic farm for a month.
   * 
   * @author Yash Hindka
   * 
   * @param farmId - farm to get milk weight for
   * @param month  - month to get milk weight for
   * @param year   - year to get milk weight for
   * @return total milk weight for specified farm during specified month.
   */
  public int getMonthWeightForFarm(String farmId, int month, int year) {

    Year specifiedYear = getSpecifiedYear(year);
    if (specifiedYear == null || specifiedYear.getMonth(month) == null) {
      return 0;
    }
    Month specified = specifiedYear.getMonth(month);
    int sum = 0;
    try {
      sum = specified.getTotalMilkWeightDateRangeMonth(1, specified.calculateMonthSize(), farmId);
    } catch (FarmNotFoundException e) {

    }
    return sum;
  }

  /**
   * Adds the data to our data structure for one file.
   * 
   * @author Moulik Mehta Resources used: https://www.baeldung.com/java-csv-file-array
   * @author Jeffrey Li Faulty Data Handling
   * 
   * @param file - file to be added
   * @return true if file successfully added, false otherwise.
   */
  public boolean addNewData(File file) {

    int counter = 0;
    int dateIndex = 0;
    int farmIdIndex = 0;
    int weightIndex = 0;

    try (Scanner scanner = new Scanner(file)) {
      while (scanner.hasNextLine()) {
        String dataEntry = scanner.nextLine();
        // The array of the data split by commas in this csv file.
        String[] data = dataEntry.split(",");

        if (counter == 0) { // The first row should hold the name of the variables.
          for (int i = 0; i < data.length; i++) {
            if (data[i].equals("date")) {
              dateIndex = i;
            } else if (data[i].equals("farm_id")) {
              farmIdIndex = i;
            } else if (data[i].equals("weight")) {
              weightIndex = i;
            }
          }
          if (dateIndex == 0 && farmIdIndex == 0 && weightIndex == 0) {
            dateIndex = 0;
            farmIdIndex = 1;
            weightIndex = 2;
          }
        } else { // This is not the first row, and we have the date, farm_id, and milkWeight
                 // to
                 // process.

          // Catches incorrect data format and skips over it
          try {
            // Data should contain three parts: date, farmID, weight
            if (data.length != 3) {
              throw new Exception();
            }
            // Check if date is valid
            SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
            toDate.setLenient(false);
            Date testDate = toDate.parse(data[dateIndex]);
            // Check if milk weight is an int
            Integer.parseInt(data[weightIndex]);
          } catch (Exception e) {
            if (file.getName().equals("AddToReport.txt")) {
              return false;
            }
            try {
              // Resource used:
              // https://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
              Files.write(Paths.get("RemovedErrorData.txt"), (dataEntry + "\n").getBytes(),
                  StandardOpenOption.APPEND);
            } catch (IOException e1) {
            }
            continue;
          }

          // Split the date up.
          String date = data[dateIndex];
          String[] dateArray = date.split("-");
          // Create these variables to then make into integers and modify them.
          String farmId = data[farmIdIndex];
          int weight;
          int year;
          int month;
          int day;
          try { // Try make these String variables stored in data array into integers.
            weight = Integer.parseInt(data[weightIndex]);
            year = Integer.parseInt(dateArray[0]);
            month = Integer.parseInt(dateArray[1]);
            day = Integer.parseInt(dateArray[2]);
          } catch (NumberFormatException e) { // If the date or weightIndex is not a
                                              // number, return
                                              // false.
            return false;
          }
          // If the year already exists, then just add the data there.
          boolean yearAlreadyAdded = false;
          for (Year yearVariable : years) {
            if (yearVariable.getYearNumber() == year) {
              yearAlreadyAdded = true;
              if (!yearVariable.insertData(month, day, year, farmId, weight)) {
                if (file.getName().equals("AddToReport.txt")) {
                  return false;
                }
                File conflictedEntry = new File("ConflictedEntries.txt");
                try {
                  conflictedEntry.createNewFile();
                  Files.write(Paths.get("ConflictedEntries.txt"), (dataEntry + "\n").getBytes(),
                      StandardOpenOption.APPEND);
                } catch (IOException e) {

                }
              }
            }
          }
          if (!yearAlreadyAdded) { // Year doesn't already exist.
            Year newYear = new Year(year);
            newYear.insertData(month, day, year, farmId, weight);
            years.add(newYear);
          }

        }
        counter++;
      }
    } catch (FileNotFoundException e) {
      return false;
    }
    return true;
  }

  /**
   * Removes the data from an uploaded file.
   * 
   * @author Albert Men
   * 
   * @param file - the file to be removed.
   * @return true of data successfully removed, false otherwise.
   * @throws FarmNotFoundException - if farm is not found
   */
  public boolean removeCurrentData(File file) throws FarmNotFoundException {
    int counter = 0;
    int dateIndex = 0;
    int farmIdIndex = 0;
    int weightIndex = 0;

    try (Scanner scanner = new Scanner(file)) {
      while (scanner.hasNextLine()) {
        String dataEntry = scanner.nextLine();
        // The array of the data split by commas in this csv file.
        String[] data = dataEntry.split(",");
        if (counter == 0) { // The first row should hold the name of the variables.
          for (int i = 0; i < data.length; i++) {
            if (data[i].equals("date")) {
              dateIndex = i;
            } else if (data[i].equals("farm_id")) {
              farmIdIndex = i;
            } else if (data[i].equals("weight")) {
              weightIndex = i;
            }
          }
        } else { // This is not the first row, and we have the date, farm_id, and milkWeight
                 // to
                 // process.
          // Split the date up.
          String date = data[dateIndex];
          String[] dateArray = date.split("-");
          // Create these variables to then make into integers and modify them.
          String farmId = data[farmIdIndex];
          int weight;
          int year;
          int month;
          int day;
          try { // Try make these String variables stored in data array into integers.
            weight = Integer.parseInt(data[weightIndex]);
            year = Integer.parseInt(dateArray[0]);
            month = Integer.parseInt(dateArray[1]);
            day = Integer.parseInt(dateArray[2]);
          } catch (NumberFormatException e) { // If date/weight not a number
            return false;
          }
          boolean yearAlreadyAdded = false;
          for (Year yearVariable : years) {
            if (yearVariable.getYearNumber() == year) {
              yearAlreadyAdded = true;
              yearVariable.removeData(month, day, year, farmId, weight); // removes the data
            }
          }
          if (!yearAlreadyAdded) { // Year doesn't already exist.
            Year newYear = new Year(year);
            newYear.removeData(month, day, year, farmId, weight);
          }

        }
        counter++;
      }
    } catch (FileNotFoundException e) {
      return false;
    }
    return true;
  }

  /**
   * Returns an ObservableList of the String data entries in each file uploaded to the GUI.
   * 
   * @author Moulik Mehta
   * 
   * @param listOfFiles the list of files that have been uploaded to the GUI.
   * @return an ObservableList that contains the entries in each file uploaded.
   * @throws FileNotFoundException if one of the files in the list of files is not found.
   */
  public ObservableList<String> getListOfAllData(List<File> listOfFiles)
      throws FileNotFoundException {
    ObservableList<String> dataEntries = FXCollections.observableArrayList();
    // Iterate through list of files and add each entry to dataEntries.
    for (int i = 0; i < listOfFiles.size(); i++) {
      File file = listOfFiles.get(i); // file for this iteration.
      try (Scanner scanner = new Scanner(file)) {
        int counter = 0;
        while (scanner.hasNextLine()) {
          if (counter != 0) { // The first line only contains the variable names.
            dataEntries.add(scanner.nextLine().trim());
          }
          counter++;
        }
      }

    }
    return dataEntries;
  }

  /**
   * @return the set of Farms.
   */
  public Set<String> uniqueFarms() {
    Set<String> unique = new HashSet<String>();
    for (int i = 0; i < years.size(); i++) {
      unique.addAll(years.get(i).uniqueFarmIDs());
    }
    return unique;
  }

  /**
   * Gets list of all years
   * 
   * @return the list of years
   */
  public List<Year> getYears() {
    return years;
  }

  /**
   * Inserts a new Year.
   * 
   * @param year to be added.
   */
  public void addYear(Year year) {
    years.add(year);
  }
}
