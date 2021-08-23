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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Creates and handles the reports of all the data including Farm Report, Annual Report, Monthly
 * Report, and Date-Range Report
 * 
 * @author everyone
 */
public class Report {

  private ReportTypes reportType; // Type of the report
  private String farmID; // Farm Report farmID
  private int year; // Year of data to get
  private boolean useAllYears; // Farm report Use All Data
  private int month; // Month of data to get
  private String startDate; // Date-range report start date
  private String endDate; // Date-range report end date
  private boolean gotReportSuccess; // True if data was successfully written
  private String[] months = {"January", "February", "March", "April", "May", "June", "July",
      "August", "September", "October", "November", "December"}; // months of year

  private File reportFile; // File that data will be written to

  String data;
  // lists of data visible to user
  private ObservableList<FarmDataWithDate> farmObservableList = FXCollections.observableArrayList();
  private ObservableList<FarmDataNoDate> annualObservableList = FXCollections.observableArrayList();
  private ObservableList<FarmDataNoDate> monthlyObservableList =
      FXCollections.observableArrayList();
  private ObservableList<FarmDataNoDate> dateRangeObservableList =
      FXCollections.observableArrayList();

  /**
   * Constructor for Farm Report
   * 
   * @param reportType  - Should be farm report (ReportTypes.FARM)
   * @param farmID      - ID of farm
   * @param year        - Year of data to use
   * @param useAllYears - True to use all years of data
   * @throws FileNotFoundException - If file isn't found
   */
  public Report(ReportTypes reportType, String farmID, int year, boolean useAllYears)
      throws FileNotFoundException {

    this.farmID = farmID;
    this.year = year;
    this.reportType = reportType;
    this.useAllYears = useAllYears;
    gotReportSuccess = false;
    reportFile = new File("FarmReport.txt");
    reportFile.delete(); // delete in case of previous data
    reportFile = new File("FarmReport.txt");
    PrintWriter printWriter = null;

    try {
      printWriter = new PrintWriter(reportFile); // Can throw a FileNotFoundException.
      printWriter.println("year,farm_id,weight,% of total weight"); // Print the String result
                                                                    // to output.txt.
    } finally {
      if (printWriter != null) {
        printWriter.close();
      }
    }
  }

  /**
   * Constructor for Annual Report
   * 
   * @param reportType - Should be annual report
   * @param year       - Year to get data
   * @throws FileNotFoundException - If file isn't found
   */
  public Report(ReportTypes reportType, int year) throws FileNotFoundException {

    this.reportType = reportType;
    this.year = year;
    gotReportSuccess = false;
    reportFile = new File("AnnualReport.txt");
    reportFile.delete(); // delete in case of previous data
    reportFile = new File("AnnualReport.txt");
    PrintWriter printWriter = null;

    try {
      printWriter = new PrintWriter(reportFile);
    } finally {
      if (printWriter != null) {
        printWriter.close();
      }
    }
  }

  /**
   * Constructor for Monthly Report
   * 
   * @param reportType - Should be monthly report
   * @param year       - Year to get data
   * @param month      - Month of year to get data
   * @throws FileNotFoundException - If file isn't found
   */
  public Report(ReportTypes reportType, int year, int month) throws FileNotFoundException {

    this.reportType = reportType;
    this.year = year;
    this.month = month;
    gotReportSuccess = false;
    reportFile = new File("MonthlyReport.txt");
    reportFile.delete(); // delete in case of previous data
    reportFile = new File("MonthlyReport.txt");
    PrintWriter printWriter = null;

    try {
      printWriter = new PrintWriter(reportFile);
    } finally {
      if (printWriter != null) {
        printWriter.close();
      }
    }
  }

  /**
   * Constructor for Date-Range Report
   * 
   * @param reportType - Should be date-range
   * @param startDate  - Start date (yyyy-mm-dd)
   * @param endDate    - End date (yyyy-mm-dd)
   * @throws FileNotFoundException - If file isn't found
   * @throws ParseException        - If one of the dates isn't formatted correctly
   * @throws ArithmeticException   - If the end date is before the start date
   */
  public Report(ReportTypes reportType, String startDate, String endDate)
      throws FileNotFoundException, ParseException, ArithmeticException {

    this.reportType = reportType;
    this.startDate = startDate;
    this.endDate = endDate;

    // Parse strings into dates
    SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
    toDate.setLenient(false);
    Date start = toDate.parse(startDate);
    Date end = toDate.parse(endDate);
    // Check if start date is after end date
    if (start.after(end)) {
      throw new ArithmeticException();
    }

    reportFile = new File("DateRangeReport.txt");
    reportFile.delete(); // delete in case of previous data
    reportFile = new File("DateRangeReport.txt");
    PrintWriter printWriter = null;

    try {
      printWriter = new PrintWriter(reportFile);
    } finally {
      if (printWriter != null) {
        printWriter.close();
      }
    }
  }

  /**
   * Returns true if data was successfully written
   * 
   * @return true if data was written successfully
   */
  public boolean gotReportSuccessfully() {
    return gotReportSuccess;
  }

  /**
   * Gets the data for the report
   * 
   * @return File of data returned
   * @throws FileNotFoundException - If file isn't found
   * @throws IOException           - If file writer throws an exception
   */
  public File getReportData() throws FileNotFoundException, IOException {
    // Check the report type and call the individual methods
    switch (reportType) {
      case FARM:
        // If users wants all years to be used, loop through all years and get report for each year
        if (this.useAllYears) {
          List<Year> years = Main.manager.getYears();
          for (Year year : years) {
            this.year = year.getYearNumber();
            this.reportFile = getFarmReport(this.reportFile);
          }
          return reportFile;
        } else {
          return getFarmReport(this.reportFile);
        }
      case ANNUAL:
        return getAnnualReport();
      case MONTHLY:
        return getMonthlyReport();
      case DATE_RANGE:
        return getDateRangeReport();
      default:
        return null;
    }
  }

  /**
   * Writes the Farm report.
   * 
   * @param reportFile - file to be read
   * 
   * @return file with results
   * @throws FileNotFoundException - no file found
   * @throws IOException
   */
  private File getFarmReport(File reportFile) throws FileNotFoundException, IOException {

    int[] monthWeights = new int[12];
    // get weights for each month for the specified farmID in the specified year
    for (int i = 0; i < monthWeights.length; i++) {
      monthWeights[i] = Main.manager.getMonthWeightForFarm(this.farmID, i, year);
    }
    // get weights for each month for all farmIDs in the specified year
    int[] totalWeights = new int[12];
    for (int i = 0; i < totalWeights.length; i++) {
      totalWeights[i] = Main.manager.getTotalMilkWeightMonth(i, this.year); // Gets total weights
    }
    // write data to file
    PrintWriter printWriter = new PrintWriter(new FileWriter(reportFile, true));
    int i = 0;
    for (int monthWeight : monthWeights) {
      String percentageString = "N/A";
      if (totalWeights[i] != 0) {
        double percentage = ((double) monthWeight / totalWeights[i]) * 100; // Calculates %
        percentageString = String.format("%.4f", percentage);
      }
      String date = this.year + "-" + months[i]; // Prints out each line
      printWriter.write(
          date + "," + this.farmID + "," + monthWeight + "," + percentageString + "%" + "\n");
      farmObservableList
          .add(new FarmDataWithDate(date, this.farmID, monthWeight, percentageString));
      i++;
    }
    printWriter.write("\n");
    printWriter.close();
    gotReportSuccess = true;
    return reportFile;
  }

  /**
   * @return the observable list of farms.
   */
  public ObservableList<FarmDataWithDate> getFarmObservableList() {
    return farmObservableList;
  }

  /**
   * Writes the Annual report.
   * 
   * @return file with results
   * @throws FileNotFoundException - no file found
   */
  private File getAnnualReport() throws FileNotFoundException {

    int totalMilkSum = 0;
    Set<String> uniqueFarms = Main.manager.uniqueFarms();
    ArrayList<String> allFarmIDList = new ArrayList<String>();
    uniqueFarms.forEach(e -> allFarmIDList.add(e)); // Adds all farms in List.
    String[] allFarmIDs = new String[allFarmIDList.size()];
    int[] minWeight = new int[allFarmIDList.size()];
    int[] maxWeight = new int[allFarmIDList.size()];
    double[] avgWeight = new double[allFarmIDList.size()];
    for (int i = 0; i < allFarmIDList.size(); i++) {
      allFarmIDs[i] = allFarmIDList.get(i);
    }
    int[] farmWeights = new int[allFarmIDs.length];
    for (int i = 0; i < farmWeights.length; i++) {
      farmWeights[i] = 0;
    }
    for (int i = 0; i < allFarmIDs.length; i++) { // Gets the max, min, average.
      int currentMinWeight = Integer.MAX_VALUE;
      int currentMaxWeight = 0;
      int totalWeightForFarm = 0;
      for (int j = 0; j <= 11; j++) {
        int newMonthWeight = Main.manager.getMonthWeightForFarm(allFarmIDs[i], j, year);
        if (newMonthWeight < currentMinWeight) {
          currentMinWeight = newMonthWeight;
        }
        if (newMonthWeight > currentMaxWeight) {
          currentMaxWeight = newMonthWeight;
        }
        totalWeightForFarm += newMonthWeight;
        farmWeights[i] += newMonthWeight;
      }
      totalMilkSum += farmWeights[i];
      minWeight[i] = currentMinWeight;
      maxWeight[i] = currentMaxWeight;
      avgWeight[i] = (double) totalWeightForFarm / (double) 12;
    }

    double[] percentages = new double[allFarmIDs.length]; // Calculates %
    for (int i = 0; i < allFarmIDs.length; i++) {
      percentages[i] = 100 * ((double) farmWeights[i] / (double) totalMilkSum);
    }
    // Write each line of data
    PrintWriter printWriter = new PrintWriter(reportFile);
    printWriter.write(
        "farmID,totalWeight,percentageOfTotal,minWeightOneMonth,maxWeightOneMonth,avgWeightPerMonth\n");
    for (int i = 0; i < allFarmIDs.length; i++) {
      if (farmWeights[i] == 0) {
        continue;
      }
      String percent = String.format("%.4f", percentages[i]);
      String avg = String.format("%.4f", avgWeight[i]);
      printWriter.write(allFarmIDs[i] + "," + farmWeights[i] + "," + percent + "%," + minWeight[i]
          + "," + maxWeight[i] + "," + avg + "\n");
      annualObservableList.add(new FarmDataNoDate(allFarmIDs[i], farmWeights[i], percent,
          minWeight[i], maxWeight[i], avg));

    }
    printWriter.close();
    gotReportSuccess = true;
    return reportFile;
  }

  /**
   * @return the annual Observable list.
   */
  public ObservableList<FarmDataNoDate> getAnnualObservableList() {
    return annualObservableList;
  }

  /**
   * Writes the Monthly report.
   * 
   * @return file with results
   * @throws FileNotFoundException - no file found
   */
  private File getMonthlyReport() throws FileNotFoundException {

    int totalMilkSum = 0;
    Set<String> uniqueFarms = Main.manager.uniqueFarms();
    ArrayList<String> allFarmIDList = new ArrayList<String>();
    uniqueFarms.forEach(e -> allFarmIDList.add(e)); // Adds each farm to List
    String[] allFarmIDs = new String[allFarmIDList.size()];
    for (int i = 0; i < allFarmIDList.size(); i++) {
      allFarmIDs[i] = allFarmIDList.get(i);
    }
    int[] farmWeights = new int[allFarmIDs.length];
    for (int i = 0; i < farmWeights.length; i++) {
      farmWeights[i] = 0;
    }
    for (int i = 0; i < allFarmIDs.length; i++) {// Gets the month weights for farms
      farmWeights[i] += Main.manager.getMonthWeightForFarm(allFarmIDs[i], month - 1, year);
      totalMilkSum += farmWeights[i];
    }

    double[] percentages = new double[allFarmIDs.length]; // Calculates %
    for (int i = 0; i < allFarmIDs.length; i++) {
      percentages[i] = 100 * ((double) farmWeights[i] / (double) totalMilkSum);
    }
    PrintWriter printWriter = new PrintWriter(reportFile);
    // Writes each line of data
    printWriter.write("farmID,totalWeight,percentageOfTotal\n");
    for (int i = 0; i < allFarmIDs.length; i++) {
      if (farmWeights[i] == 0) {
        continue;
      }
      String percent = String.format("%.4f", percentages[i]);
      printWriter.write(allFarmIDs[i] + "," + farmWeights[i] + "," + percent + "%" + "\n");
      monthlyObservableList.add(new FarmDataNoDate(allFarmIDs[i], farmWeights[i], percent));
    }
    printWriter.close();
    gotReportSuccess = true;
    return reportFile;
  }

  /**
   * @return the monthly observable list.
   */
  public ObservableList<FarmDataNoDate> getMonthlyObservableList() {
    return monthlyObservableList;
  }

  /**
   * Writes the Date Range report.
   * 
   * @return file with results
   * @throws FileNotFoundException - no file found
   */
  private File getDateRangeReport() throws FileNotFoundException {
    int totalMilkSum = 0;
    Set<String> uniqueFarms = Main.manager.uniqueFarms();
    ArrayList<String> allFarmIDList = new ArrayList<String>();
    uniqueFarms.forEach(e -> allFarmIDList.add(e)); // Gets each unique farm
    String[] allFarmIDs = new String[allFarmIDList.size()];
    for (int i = 0; i < allFarmIDList.size(); i++) {
      allFarmIDs[i] = allFarmIDList.get(i);
    }
    String[] startDayString = startDate.split("-");
    String[] endDayString = endDate.split("-");
    int[] startDay = new int[3];
    int[] endDay = new int[3];
    for (int i = 0; i < 3; i++) {
      startDay[i] = Integer.parseInt(startDayString[i]);
      endDay[i] = Integer.parseInt(endDayString[i]);
    } // Parses the user input

    int[] farmWeights = new int[allFarmIDs.length];
    for (int i = 0; i < farmWeights.length; i++) {
      farmWeights[i] = 0;
    }
    for (int i = 0; i < allFarmIDs.length; i++) { // gets weight between dates
      farmWeights[i] += Main.manager.getDateRangeMilkWeightFarm(allFarmIDs[i], startDay[0],
          startDay[1], startDay[2], endDay[0], endDay[1], endDay[2]);
      totalMilkSum += farmWeights[i];
    }

    double[] percentages = new double[allFarmIDs.length]; // Caluculates %
    for (int i = 0; i < allFarmIDs.length; i++) {
      percentages[i] = 100 * ((double) farmWeights[i] / (double) totalMilkSum);
    }
    PrintWriter printWriter = new PrintWriter(reportFile);
    // Writes each line of data
    printWriter.write("farmID,totalWeight,percentageOfTotal\n");
    for (int i = 0; i < allFarmIDs.length; i++) {
      if (farmWeights[i] == 0) {
        continue;
      }
      String percent = String.format("%.4f", percentages[i]);
      printWriter.write(allFarmIDs[i] + "," + farmWeights[i] + "," + percent + "%" + "\n");
      dateRangeObservableList.add(new FarmDataNoDate(allFarmIDs[i], farmWeights[i], percent));
    }
    printWriter.close();
    gotReportSuccess = true;
    return reportFile;
  }

  /**
   * @return the Date Range Observable List.
   */
  public ObservableList<FarmDataNoDate> getDateRangeObservableList() {
    return dateRangeObservableList;
  }

}
