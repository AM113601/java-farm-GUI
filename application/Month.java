/**
 * Project: aTeam Final Project 
 * Description: Program for reading milk weight inputs and displaying data tables of milk categories 
 * Authors: Jeffrey Li, Albert Men, Moulik Mehta, Yash Hindka 
 * Email: jkli@wisc.edu, awmen@wisc.edu, moulik.mehta@wisc.edu, yhindka@wisc.edu
 * 
 * Course: CS400 Semester: Spring 2020 Lecture: LEC001
 */

package application;

import java.util.HashSet;
import java.util.Set;

/**
 * This class holds the Farm Data for all farms in one month
 * 
 * @author Albert Men
 *
 */
public class Month {

  private int monthID; // ID of the month (0 = January, 1 = February, ...)
  private int year; // Year that the month is in
  private FarmDataByMonth[] farms; // Holds the FarmData objects
  private String[] farmIDs; // Holds the corresponding farmIDs to farms

  /**
   * Constructor for Month
   * 
   * @param monthID       - ID of month (int representing month)
   * @param year          - year of the month
   * @param startingFarms - number of starting farms
   */
  public Month(int monthID, int year, int startingFarms) {
    this.monthID = monthID;
    farms = new FarmDataByMonth[startingFarms];
    farmIDs = new String[startingFarms];
  }

  /**
   * Gets the array of farms
   * 
   * @return Array of farms
   */
  public FarmDataByMonth[] getFarms() {
    return farms;
  }

  /**
   * Gets the total milk weight for one farm within a month
   * 
   * @author Albert Men
   * 
   * @param farmID - ID of farm
   * @return total milk weight
   * @throws FarmNotFoundException - if farmID doesn't exist
   */
  public int getTotalMilkWeightOneFarm(String farmID) throws FarmNotFoundException {
    int farmIndex = -1;
    // Search for farmID
    for (int i = 0; i < farmIDs.length; i++) {
      if (farmIDs[i] == null) {
        continue;
      }
      if (farmIDs[i].equals(farmID)) {
        farmIndex = i;
        break;
      }
    }
    // If farm wasn't found throw exception
    if (farmIndex == -1) {
      throw new FarmNotFoundException();
    }
    return farms[farmIndex].getTotalMilkWeightFarm();
  }

  /**
   * Gets the Date-Range milk weight within a month for a specific farm
   * 
   * @author Albert Men
   * 
   * @param startDay - Start date
   * @param endDay   - End date
   * @param farmID   - ID of farm
   * @return Total milk weight
   * @throws FarmNotFoundException - if farmID doesn't exist
   */
  public int getTotalMilkWeightDateRangeMonth(int startDay, int endDay, String farmID)
      throws FarmNotFoundException {
    int farmIndex = -1;
    // Search for farmID
    for (int i = 0; i < farmIDs.length; i++) {
      if (farmIDs[i] == null) {
        continue;
      }
      if (farmIDs[i].equals(farmID)) {
        farmIndex = i;
        break;
      }
    }
    // If farm wasn't found throw exception
    if (farmIndex == -1) {
      throw new FarmNotFoundException();
    }
    return farms[farmIndex].getTotalMilkWeightFarm(startDay, endDay);
  }

  /**
   * Returns the total milk weight for all farms in this month
   * 
   * @return Total milk weight
   */
  public int getTotalMilkWeightMonth() {
    int total = 0;
    // Loop through all farms and add their milk weight
    for (FarmDataByMonth i : farms) {
      if (i != null) {
        total += i.getTotalMilkWeightFarm();
      }
    }
    return total;
  }

  /**
   * Inserts milk for a farm on one specific day
   * 
   * @author Albert Men
   * 
   * @param farmID     - ID of the farm
   * @param milkWeight - Amount of milk
   * @param day        - Day to insert
   */
  public boolean insertMilkWeightForFarm(String farmID, int milkWeight, int day) {
    int farmIndex = -1;
    int earliestNull = -1; // Keeps track of the earliest spot where there isn't a farm
    boolean foundNull = false;
    // Loop through all farms to see if farm already exists
    for (int i = 0; i < farmIDs.length; i++) {
      // If current is null, check if we've already found a null
      if (farmIDs[i] == null) {
        // If already found, skip, otherwise make it the earliest null
        if (foundNull)
          continue;
        else {
          earliestNull = i;
          foundNull = true;
          continue;
        }
      }
      // If farm is found
      if (farmIDs[i].equals(farmID)) {
        farmIndex = i;
        break;
      }
    }

    // If no farm pre-exists create new farm
    if (farmIndex == -1) {
      // If there are no more open spaces, increase array size
      if (earliestNull == -1) {
        earliestNull = farms.length;
        increaseFarmSize();
      }
      // Create new farm and set corresponding farmID
      farms[earliestNull] = new FarmDataByMonth(calculateMonthSize());
      farmIDs[earliestNull] = farmID;
      farmIndex = earliestNull;
    }
    if (farms[farmIndex].getMilkWeight(day) != 0) {
      return false;
    }
    farms[farmIndex].setMilkWeight(milkWeight, day);
    return true;
  }

  /**
   * Doubles farm size to allow for more farms
   * 
   * @author Albert Men
   */
  private void increaseFarmSize() {
    FarmDataByMonth[] oldFarmData = farms;
    String[] oldFarmIDs = farmIDs;

    // Double array size
    farms = new FarmDataByMonth[farms.length * 2];
    farmIDs = new String[farmIDs.length * 2];

    // Add old data into new array
    for (int i = 0; i < oldFarmData.length; i++) {
      farms[i] = oldFarmData[i];
      farmIDs[i] = oldFarmIDs[i];
    }
  }

  /**
   * Removes the milk weight for one day in a farm
   * 
   * @param farmID - ID of the farm
   * @param day    - Day to remove
   * @throws FarmNotFoundException - If farm cannot be found
   */
  public void removeMilkWeightForFarm(String farmID, int day, int weight)
      throws FarmNotFoundException {
    int index = -1;
    // Search for farm
    for (int i = 0; i < farmIDs.length; i++) {
      if (farmID.equals(farmIDs[i])) {
        index = i;
        break;
      }
    }
    // If farm cannot be found, throw exception
    if (index == -1)
      throw new FarmNotFoundException();
    farms[index].removeMilkWeight(day, weight);
  }

  /**
   * Calculates the number of days in the current month
   * 
   * @author Albert Men
   * 
   * @return Number of days in the month
   */
  public int calculateMonthSize() {
    switch (monthID) {
      case 0:
        return 31;
      case 1:
        // Calculate if February is 29 or 28 days
        if (leapYear()) {
          return 29;
        } else {
          return 28;
        }
      case 2:
        return 31;
      case 3:
        return 30;
      case 4:
        return 31;
      case 5:
        return 30;
      case 6:
        return 31;
      case 7:
        return 31;
      case 8:
        return 30;
      case 9:
        return 31;
      case 10:
        return 30;
      case 11:
        return 31;
      default:
        return -1;
    }
  }

  /**
   * Checks if current year is a leap year
   * 
   * @author Albert Men
   * 
   * @return True if leapyear false otherwise
   */
  private boolean leapYear() {
    if (year % 400 == 0) {
      return true;
    } else if (year % 100 == 0) {
      return false;
    } else if (year % 4 == 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Return the ID of the month
   * 
   * @author Albert Men
   * 
   * @return monthID
   */
  public int getMonth() {
    return monthID;
  }

  /**
   * Gets all the Farm IDs.
   * 
   * @return a set of FarmIDs.
   */
  public Set<String> allFarmIDs() {
    Set<String> allFarms = new HashSet<String>();
    for (int i = 0; i < farmIDs.length; i++) {
      if (farmIDs[i] != null) {
        allFarms.add(farmIDs[i]);
      }
    }
    return allFarms;
  }
}
