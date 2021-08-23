/**
 * Project: aTeam Final Project Description: Program for reading milk weight inputs and displaying
 * data tables of milk categories Authors: Jeffrey Li, Albert Men, Moulik Mehta, Yash Hindka Email:
 * jkli@wisc.edu, awmen@wisc.edu, moulik.mehta@wisc.edu, yhindka@wisc.edu
 * 
 * Course: CS400 Semester: Spring 2020 Lecture: LEC001
 */

package application;

/**
 * @author Moulik Mehta
 * 
 *         Class that is used for TableView. Contains the date, farm id, total
 *         weight, and the percentage of total weight.
 *
 */
public class FarmDataWithDate {
	private String farmID;
	private int totalWeight;
	private String percentageOfTotal;
	private String date;

	/**
	 * FarmDataWithDate Constructor.
	 * 
	 * @param date              - date
	 * @param farmID            - farmID
	 * @param totalWeight       - total weight
	 * @param percentageOfTotal - percentage
	 */
	public FarmDataWithDate(String date, String farmID, Integer totalWeight, String percentageOfTotal) {
		this.date = date;
		this.farmID = farmID;
		this.totalWeight = totalWeight;
		this.percentageOfTotal = percentageOfTotal;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the farmID
	 */
	public String getFarmID() {
		return farmID;
	}

	/**
	 * @param farmId the farmId to set
	 */
	public void setFarmID(String farmID) {
		this.farmID = farmID;
	}

	/**
	 * @return the totalWeight
	 */
	public int getTotalWeight() {
		return totalWeight;
	}

	/**
	 * @param totalWeight the totalWeight to set
	 */
	public void setTotalWeight(int totalWeight) {
		this.totalWeight = totalWeight;
	}

	/**
	 * @return the percentageOfTotal
	 */
	public String getPercentageOfTotal() {
		return percentageOfTotal;
	}

	/**
	 * @param percentageOfTotal the percentageOfTotal to set
	 */
	public void setPercentageOfTotal(String percentageOfTotal) {
		this.percentageOfTotal = percentageOfTotal;
	}

}
