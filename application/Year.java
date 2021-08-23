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
 * Represents a year of milk data. Holds the months in the year.
 * 
 * @author everyone
 *
 */
public class Year {

  private Month[] months; // Months in the year
  private int year; // Year number

  /**
   * Constructor of Year.
   * 
   * @param year - the year number
   */
  public Year(int year) {
    months = new Month[12]; // 12 months in year
    this.year = year;
  }

  /**
   * Returns the year.
   * 
   * @return the year
   */
  public int getYearNumber() {
    return year;
  }

  /**
   * Gets Milk Weights for 1 year.
   * 
   * @return the total milk weight
   */
  public int getTotalMilkWeightYear() {
    int sum = 0;
    for (Month month : months) {
      sum += month.getTotalMilkWeightMonth();
    }
    return sum;
  }

  /**
   * Inserts Milk weight for specified farm and date.
   * 
   * @author Albert Men
   * 
   * @param month      - the month to be inserted in.
   * @param day        - the day to be inserted in.
   * @param farmId     - the corresponding farm.
   * @param milkWeight - how much milkWeight to be added.
   * @return true if successful, false otherwise
   */
  public boolean insertData(int month, int day, int year, String farmId, int milkWeight) {
    if (months[month - 1] == null) { // if no month created
      months[month - 1] = new Month(month - 1, year, 1);
    }
    if (months[month - 1].insertMilkWeightForFarm(farmId, milkWeight, day))
      return true;
    else
      return false;
  }

  /**
   * Removes Milk weight for specified farm and date.
   * 
   * @author Albert Men
   * 
   * @param month
   * @param day
   * @param year
   * @param farmId
   * @param milkWeight
   * @throws FarmNotFoundException
   * @returns true if successful, false otherwise
   */
  public boolean removeData(int month, int day, int year, String farmId, int milkWeight)
      throws FarmNotFoundException {
    if (months[month - 1] != null) { // if the month exists
      months[month - 1].removeMilkWeightForFarm(farmId, day, milkWeight);
      return true;
    }
    return false;
  }

  /**
   * Returns month specified.
   * 
   * @param month - int value of month (0 = January, 11 = December)
   * @return Month specified
   */
  public Month getMonth(int month) {
    return months[month];
  }

  /**
   * Returns a set of all the unique farmIDs in this year
   * 
   * @author Jeffrey Li
   * @return Set of farmIDs
   */
  public Set<String> uniqueFarmIDs() {
    Set<String> unique = new HashSet<String>();
    for (int i = 0; i < months.length; i++) {
      if (months[i] != null) {
        unique.addAll(months[i].allFarmIDs());
      }
    }
    return unique;
  }

}
