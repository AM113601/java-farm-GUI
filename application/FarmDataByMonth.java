/**
 * Project: aTeam Final Project 
 * Description: Program for reading milk weight inputs and displaying data tables of milk categories 
 * Authors: Jeffrey Li, Albert Men, Moulik Mehta, Yash Hindka 
 * Email: jkli@wisc.edu, awmen@wisc.edu, moulik.mehta@wisc.edu, yhindka@wisc.edu
 * 
 * Course: CS400 Semester: Spring 2020 Lecture: LEC001
 */

package application;

/**
 * This class holds the amount of milk for one farm in one month
 * 
 * @author Albert Men & Yash Hindka
 *
 */
public class FarmDataByMonth {

  private int[] milkWeights; // Holds the month's milk weight for a farm

  /**
   * Constructor for FarmDataByMonth
   * 
   * @param monthSize - size of the month
   */
  public FarmDataByMonth(int monthSize) {
    milkWeights = new int[monthSize]; // calculated in month class
  }

  /**
   * Gets the total milk weight during the month for this farm
   * 
   * @return Total milk weight
   */
  public int getTotalMilkWeightFarm() {
    int sum = 0;
    for (int weight : milkWeights) {
      sum += weight;
    }
    return sum;
  }

  /**
   * Gets the total milk weight for a date range in a month. Both days are inclusive
   * 
   * @param start - start day
   * @param end   - end day
   * @return - Total milk weight between the two days
   */
  public int getTotalMilkWeightFarm(int start, int end) {
    int sum = 0;
    for (int i = start - 1; i < end; i++) {
      sum += milkWeights[i];
    }
    return sum;
  }

  /**
   * Sets the milk weight for a specific day
   * 
   * @param setmilkWeights - Amount of milk
   * @param day            - Day to set
   */
  public void setMilkWeight(int setmilkWeights, int day) {
    milkWeights[day - 1] = setmilkWeights;
  }

  /**
   * Adds milk to preexisting amount of milk
   * 
   * @param addMilkWeights - Amount to add
   * @param day            - Day to set
   */
  public void addMilkWeight(int addMilkWeights, int day) {
    milkWeights[day - 1] += addMilkWeights;
  }

  public int getMilkWeight(int day) {
    return milkWeights[day - 1];
  }

  /**
   * Removes milk of a day
   * 
   * @param day - Day to remove
   */
  public void removeMilkWeight(int day, int weight) {
    milkWeights[day - 1] = 0;
  }

}
