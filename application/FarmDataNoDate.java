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
 *         Class that is used for TableView. Contains the farm id, total weight,
 *         and the percentage of total weight.
 *
 */
public class FarmDataNoDate {
	private String farmID;
	private int totalWeight;
	private String percentageOfTotal;
	private int minWeightOneMonth;
	private int maxWeightOneMonth;
	private String avgWeightPerMonth;

	/**
	 * Constructor of FarmDataNoDate.
	 * 
	 * @param farmID            - farmID
	 * @param totalWeight       - total weight of farm
	 * @param percentageOfTotal - percentage of total
	 */
	public FarmDataNoDate(String farmID, Integer totalWeight, String percentageOfTotal) {
		this.farmID = farmID;
		this.totalWeight = totalWeight;
		this.percentageOfTotal = percentageOfTotal;
	}

	/**
	 * Constructor of FarmDataNoDate.
	 * 
	 * @param farmID            - farmID
	 * @param totalWeight       - total weight of farm
	 * @param percentageOfTotal - percentage of total
	 */
	public FarmDataNoDate(String farmID, Integer totalWeight, String percentageOfTotal, Integer minWeight,
			Integer maxWeight, String avgWeight) {
		this.farmID = farmID;
		this.totalWeight = totalWeight;
		this.percentageOfTotal = percentageOfTotal;
		this.minWeightOneMonth = minWeight;
		this.maxWeightOneMonth = maxWeight;
		this.avgWeightPerMonth = avgWeight;
	}

	/**
	 * @return the farmID
	 */
	public String getFarmID() {
		return farmID;
	}

	/**
	 * @param farmID the farmID to set
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

	public int getMinWeightOneMonth() {
		return minWeightOneMonth;
	}

	public void setMinWeightOneMonth(int minWeight) {
		this.minWeightOneMonth = minWeight;
	}

	public int getMaxWeightOneMonth() {
		return maxWeightOneMonth;
	}

	public void setMaxWeightOneMonth(int maxWeight) {
		this.maxWeightOneMonth = maxWeight;
	}

	public String getAvgWeightPerMonth() {
		return avgWeightPerMonth;
	}

	public void setAvgWeightPerMonth(String avgWeight) {
		this.avgWeightPerMonth = avgWeight;
	}
}
