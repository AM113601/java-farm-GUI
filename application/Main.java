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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Creates the JavaFX application and handles the user interface
 * 
 * @author Jeffrey Li, Albert Men, Moulik Mehta, Yash Hindka
 */
public class Main extends Application {
  private List<File> listOfAllFiles = new ArrayList<File>();
  private List<File> uploadedFileList = new ArrayList<File>();
  // stores entries that the user deletes from observable list
  private ArrayList<String> deletedEntries = new ArrayList<>();
  private ArrayList<String> addedEntries = new ArrayList<>();
  public static FarmDataManager manager = new FarmDataManager();
  private static final int WINDOW_WIDTH = 1200;
  private static final int WINDOW_HEIGHT = 600;
  private static final String APP_TITLE = "Milk Data Manager";

  /**
   * First method that executes to set up JavaFX GUI.
   * 
   * @param primaryStage - Stage that user will see
   */
  @Override
  public void start(Stage primaryStage) throws Exception {

    File previousDeleted = new File("RemovedErrorData.txt");
    previousDeleted.delete(); // Delete file from previous run
    File newDeleted = new File("RemovedErrorData.txt");
    newDeleted.createNewFile();

    File conflictedEntry = new File("ConflictedEntries.txt");
    conflictedEntry.delete();

    BorderPane root = new BorderPane();
    Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

    // Need separate scene for instructions page
    VBox instructionBox = new VBox();
    instructionBox.getChildren().add(new Label("Instructions"));
    Scene instructionScene = new Scene(instructionBox, WINDOW_WIDTH, WINDOW_HEIGHT);

    primaryStage.setTitle(APP_TITLE);
    primaryStage.setScene(mainScene);
    primaryStage.show();

    // set various sections of BorderPane
    rootSetLeft(root, primaryStage, mainScene);
    rootSetRight(root, mainScene, instructionScene, instructionBox, primaryStage);
    rootSetCenter(root);
    rootSetBottom(root, mainScene, primaryStage);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * @author Albert Men Left-panel design of GUI.
   * 
   * @param root         - BorderPane
   * @param primaryStage - current stage
   */
  private void rootSetLeft(BorderPane root, Stage primaryStage, Scene mainScene) {
    ListView<String> listOfFileNames = new ListView<String>(); // A scene control element.
    ObservableList<String> observableListFiles = FXCollections.observableArrayList(); // List of
                                                                                      // file names

    Button browseButton = new Button("Select csv files");
    browseButton.setTextAlignment(TextAlignment.CENTER);

    // When the button is clicked, add each file that the user selects to the
    // observable list.
    browseButton.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      // User can select multiple files in one go.
      List<File> listOfSelectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
      // For each file in the list of selected files, add the actual File object to
      // the
      // listOfAllFiles, and just the name to the observable list.
      try {
        for (File file : listOfSelectedFiles) {
          if (file != null) {
            observableListFiles.add(file.getName());
            listOfAllFiles.add(file);
          }
        }
      } catch (NullPointerException e1) {

      }
      // Makes it so the user can see the updated ListView element, which is the
      // listOfFileNames.
      listOfFileNames.setItems(observableListFiles);
    });

    // The label that shows the name of the file that the user has selected.
    Label selected = new Label();

    // When the user selects an element in the list, set the text of the label to
    // this element.
    listOfFileNames.setOnMouseClicked((e) -> {
      selected.setText(listOfFileNames.getSelectionModel().getSelectedItem());
    });

    // Button allows the user to delete a selected file.
    Button delete1 = new Button("Delete");
    delete1.setOnAction(e -> {
      try {
        String selectedItem = selected.getText();
        if (!selectedItem.equals("") && selectedItem != null) {
          observableListFiles.remove(selectedItem); // Remove it from the observable list.
          listOfFileNames.setItems(observableListFiles); // Make it so that the ListView
                                                         // is
                                                         // updated.
          // Delete the corresponding file from the listOfAllFiles.
          for (int i = 0; i < listOfAllFiles.size(); i++) {
            if (listOfAllFiles.get(i).getName().equals(selectedItem)) {
              listOfAllFiles.remove(i);
            }
          }
          // After deleting, make sure the label shows nothing selected anymore.
          selected.setText("");
        }
      } catch (NullPointerException e1) {
        // if nothing is selected
      }
    });

    ListView<String> uploadedList = new ListView<String>();
    ObservableList<String> uploaded = FXCollections.observableArrayList();
    uploadedList.setItems(uploaded);

    // Button allows the user to add a specific file.
    Button upload = new Button("Upload");
    upload.setOnAction(e -> {
      String selectedItem = selected.getText(); // gets selected text
      // boolean fileSelected = false; // there is a file selected
      if (selectedItem != "" && selectedItem != null && selectedItem.contains(".csv")
          && !uploaded.contains(selectedItem)) {

        for (int i = 0; i < listOfAllFiles.size(); i++) {
          if (listOfAllFiles.get(i).getName().contentEquals(selectedItem)) {
            uploadedFileList.add(listOfAllFiles.get(i));
            manager.addNewData(listOfAllFiles.get(i));
          }
        }
        uploaded.add(selectedItem);
        uploadedList.setItems(uploaded);
      }
    });
    Label selectedUploaded = new Label();
    // When the user selects an element in the list, set the text of the label to
    // this element.
    uploadedList.setOnMouseClicked((e) -> {
      selectedUploaded.setText(uploadedList.getSelectionModel().getSelectedItem());
    });

    Button deleteUploaded = new Button("Delete Uploaded");
    deleteUploaded.setOnAction(e -> {
      try {
        String selectedItem = selectedUploaded.getText();
        if (!selectedItem.equals("") && selectedItem != null) {
          uploaded.remove(selectedItem); // Remove it from the observable list.
          uploadedList.setItems(uploaded); // Make it so that the ListView is updated.
          // Delete the corresponding file from the listOfAllFiles.
          for (int i = 0; i < uploadedFileList.size(); i++) {
            if (uploadedFileList.get(i).getName().equals(selectedItem)) {
              try {
                manager.removeCurrentData(uploadedFileList.get(i));
              } catch (FarmNotFoundException e1) {

              }
              uploadedFileList.remove(i);
            }
          }
          // After deleting, make sure the label shows nothing selected anymore.
          selected.setText("");
        }
      } catch (NullPointerException e1) {
        // if nothing is selected
      }
    });

    // Display the controls.
    HBox hBox = new HBox(browseButton, delete1);
    HBox hBox2 = new HBox(upload, selected);
    HBox hBox3 = new HBox(deleteUploaded, selectedUploaded);
    VBox vBox = new VBox(hBox, listOfFileNames, hBox2, uploadedList, hBox3);
    Label spacing = new Label("");
    Label spacing2 = new Label("");
    Button viewRemoved = new Button("View Error Entries");
    Label spacing3 = new Label("");
    Label spacing4 = new Label("");
    Button viewConflicted = new Button("View Conflicted Entries");
    viewRemoved.setOnAction(e -> {
      setSkippedEntriesScene(mainScene, primaryStage);
    });
    viewConflicted.setOnAction(e -> {
      setConflictedEntriesScene(mainScene, primaryStage);
    });
    VBox vBox2 = new VBox(vBox, spacing, spacing2, spacing3, viewRemoved, spacing4, viewConflicted);

    root.setLeft(vBox2);
  }

  /**
   * Scene to view the Skipped Entries.
   * 
   * @author Jeffrey Li
   * 
   * @param mainScene    - current Scene
   * @param primaryStage - current Stage
   */
  private void setSkippedEntriesScene(Scene mainScene, Stage primaryStage) {
    Label dataLabel =
        new Label("These entries were removed because they were in the wrong format:");
    VBox reportListBox = new VBox();
    ListView<String> listOfEntries = new ListView<String>(); // A scene control element.
    List<File> deletedEntriesList = new ArrayList<File>();
    File removedErrorFile = new File("RemovedErrorData.txt");// File to store skipped entries
    ObservableList<String> dataEntries = FXCollections.observableArrayList();
    try {
      removedErrorFile.createNewFile();
      deletedEntriesList.add(removedErrorFile);// adds data to list
      dataEntries = manager.getListOfAllData(deletedEntriesList);
    } catch (IOException e) {

    }
    listOfEntries.setItems(dataEntries);// updates list of entries
    Button backButton = new Button("Back");
    backButton.setOnAction(e -> primaryStage.setScene(mainScene));// brings back to main menu

    // Display
    reportListBox = new VBox(dataLabel, listOfEntries, backButton);
    Scene deletedEntriesScene = new Scene(reportListBox, WINDOW_WIDTH, WINDOW_HEIGHT);
    primaryStage.setScene(deletedEntriesScene);
  }

  /**
   * Scene to view conflicted entries. Catches Duplicate entries.
   * 
   * @author Jeffrey Li
   * 
   * @param mainScene    - current scene
   * @param primaryStage - current stage
   */
  private void setConflictedEntriesScene(Scene mainScene, Stage primaryStage) {
    Label dataLabel = new Label(
        "These entries were removed because there were already milk weights existing on the same day for the same farm:");
    VBox reportListBox = new VBox();
    ListView<String> listOfEntries = new ListView<String>(); // A scene control element.
    List<File> conflictedEntriesList = new ArrayList<File>();
    File conflictedDataFile = new File("ConflictedEntries.txt");
    ObservableList<String> dataEntries = FXCollections.observableArrayList();
    try {
      conflictedDataFile.createNewFile();
      conflictedEntriesList.add(conflictedDataFile);
      dataEntries = manager.getListOfAllData(conflictedEntriesList); // adds data to backend
    } catch (IOException e) {

    }
    listOfEntries.setItems(dataEntries);
    Button backButton = new Button("Back");
    backButton.setOnAction(e -> primaryStage.setScene(mainScene));

    // Display
    reportListBox = new VBox(dataLabel, listOfEntries, backButton);
    Scene conflictedEntriesScene = new Scene(reportListBox, WINDOW_WIDTH, WINDOW_HEIGHT);
    primaryStage.setScene(conflictedEntriesScene);
  }

  /**
   * Sets right pane of dashboard BorderPane, which will host a button that can take the user to the
   * instructions page
   * 
   * @author Yash Hindka
   * 
   * @param root             - BorderPane for dashboard
   * @param instructionScene - Scene for instructions page
   * @param primaryStage     - Stage for dashboard
   */
  private void rootSetRight(BorderPane root, Scene mainScene, Scene instructionScene,
      VBox instructionBox, Stage primaryStage) {

    Label infoLabel = new Label("Read first:");
    ImageView instructionImage = new ImageView(new Image("infoicon.png"));
    instructionImage.setFitHeight(20);
    instructionImage.setFitWidth(20);
    Button instructionButton = new Button("Instructions", instructionImage);

    // to allow button to take user to instructions page
    instructionButton.setOnAction(
        e -> setInstructionScene(primaryStage, instructionScene, mainScene, instructionBox));

    VBox vbox = new VBox(infoLabel, instructionButton);
    vbox.setAlignment(Pos.CENTER);
    root.setRight(vbox);
  }

  /**
   * Sets the instruction scene.
   * 
   * @author Albert Men
   * 
   * @param primaryStage     - current stage
   * @param instructionScene - instruction scene
   * @param mainScene        - main menu scene
   * @param instructionBox   - display feature.
   */
  private void setInstructionScene(Stage primaryStage, Scene instructionScene, Scene mainScene,
      VBox instructionBox) {
    primaryStage.setScene(instructionScene);
    instructionBox.getChildren().clear();
    Label howToUpload0 = new Label("\n");
    Label howToUpload1 = new Label("To upload a file:\n");
    Label howToUpload2 = new Label(
        "Click the \"Select csv file\" button. The file will appear on the top left list. "
            + "\nTo upload the data inside the analyzer, click on the file and press the \"Upload\" button."
            + "\nThe resulting file uploaded will be in the bottom left list.");
    Label howToUpload3 =
        new Label("The bottom left list contains all of the files used in the analyzer. "
            + "\nIf you wish to add additional files, choose a file from the top left list and press Upload. "
            + "\nIf you wish to delete a file click on the file within the bottom left list and click the \"Delete Uploaded\" Button. "
            + "\n*Note: if the file is not csv, it can not be uploaded!"
            + "\n*Note: files with the same name cannot be uploaded even if they are from two different locations."
            + "\n*Note: if some data entries are in the wrong format, it will be skipped and not be added into the analyzer. Instead, it can be viewed"
            + " with the \"View Error Entries\" Button. Error"
            + "\n           entries are also saved to RemovedErrorData.txt. The correct format is \"date,farmID,weight\" with date in the form of \"yyyy-mm-dd\""
            + "\n*Note: csv files should have their first line be \"date,farm_id,weight\""
            + "\n*Note: if multiple lines contain milk for the same farm on the same day, only the first one will be kept. "
            + "All proceeding data on the same day will be skipped "
            + "\n           and can be viewed by clicking the \"View Conflicted Entries\" Button");
    howToUpload1.setStyle("-fx-background-color: #FF0000");

    Label howToGenReport0 = new Label("\n");
    Label howToGenReport1 = new Label("To generate a report:\n");
    Label howToGenReport2 =
        new Label("Click the corresponding button on what report you want to generate. "
            + "\nOnce clicked, it will bring up a list of all the data entered."
            + "\nType in the information needed for the specific report in the space provided, and click on the \"Generate Report\" Button."
            + "\nTo sort data, press on the label at the top of the column that you wish the data to be sorted by."
            + "\nThe reports will be saved in a txt file named FarmReport.txt, AnnualReport.txt, MonthlyReport.txt, and DateRangeReport.txt."
            + "\n*Note: only the last report of each type will be stored inside of each txt file!");
    howToGenReport1.setStyle("-fx-background-color: #00E8FF");

    Label howToAddData0 = new Label("\n");
    Label howToAddData1 = new Label("To add/delete/update a single piece of data:\n");
    Label howToAddData2 = new Label("Click on the report you want to generate."
        + "\nOnce clicked, it will bring up a list of all the data entered."
        + "\nTo add data, enter the piece of data in the specified format, and press the \"Add\" Button."
        + "\nTo delete data, click on the piece of data in the list, or type it out, and press the \"Delete\" Button."
        + "\nTo update data, click on the piece of data in the list to update, modify the data to your liking, and then press the \"Update\" Button."
        + "\n*Note: single data is one-time use. After you press the \"Back\" Button or \"Generate Report\" Button, the data will return to its original state.");
    howToAddData1.setStyle("-fx-background-color: #59FF00");

    Button backButton = new Button("Back");
    backButton.setOnAction(e -> primaryStage.setScene(mainScene));
    instructionBox.getChildren().add(howToUpload0);
    instructionBox.getChildren().add(howToUpload1);
    instructionBox.getChildren().add(howToUpload2);
    instructionBox.getChildren().add(howToUpload3);
    instructionBox.getChildren().add(howToGenReport0);
    instructionBox.getChildren().add(howToGenReport1);
    instructionBox.getChildren().add(howToGenReport2);
    instructionBox.getChildren().add(howToAddData0);
    instructionBox.getChildren().add(howToAddData1);
    instructionBox.getChildren().add(howToAddData2);
    instructionBox.getChildren().add(backButton);
  }

  /**
   * Sets the center of the BorderPane, which is a welcome message.
   * 
   * @author Moulik Mehta
   * 
   * @param root is the BorderPane containing the main scene.
   */
  private void rootSetCenter(BorderPane root) {
    Label welcomeLabel = new Label("Welcome to Milk Weights Analyzer!");
    welcomeLabel.setWrapText(true);
    welcomeLabel.setStyle("-fx-font: 24 arial;");

    Label url = new Label("https://www.dw.com/image/50907943_303.jpg");

    Label contributorLabel =
        new Label("Created by Albert Men, Jeffrey Li, Yash Hindka, Moulik Mehta");
    contributorLabel.setWrapText(true);
    contributorLabel.setStyle("-fx-font: 14 arial;");

    Label emptyLabel = new Label(""); // For spacing.

    ImageView imageView = new ImageView(new Image("cow.jpg"));
    imageView.setFitHeight(200);
    imageView.setFitWidth(300);
    imageView.setPreserveRatio(true);

    imageView.setOnMouseEntered(e -> url.setText("Moo!"));
    imageView.setOnMouseExited(e -> url.setText("https://www.dw.com/image/50907943_303.jpg"));

    VBox vbox = new VBox();
    vbox.getChildren().addAll(welcomeLabel, imageView, url, emptyLabel, contributorLabel);
    vbox.setAlignment(Pos.CENTER);
    root.setCenter(vbox);
  }

  /**
   * Sets the bottom of the BorderPane, which contain the buttons for accessing each report
   * 
   * @author Jeffrey Li
   * 
   * @param root            - BorderPane for dashboard
   * @param mainScene       - Home (Mainscreen) scene
   * @param primaryStage    - Stage for dashboard
   * @param farmReportScene - Scene for farm report data
   * @param farmReportBox   - VBox for holding farm report data
   */
  private void rootSetBottom(BorderPane root, Scene mainScene, Stage primaryStage) {

    ImageView farmImage = new ImageView(new Image("farmicon.png"));
    farmImage.setFitHeight(30);
    farmImage.setFitWidth(30);
    ImageView annualImage = new ImageView(new Image("annualicon.png"));
    annualImage.setFitHeight(30);
    annualImage.setFitWidth(30);
    ImageView monthImage = new ImageView(new Image("monthicon.png"));
    monthImage.setFitHeight(30);
    monthImage.setFitWidth(30);
    ImageView dateRangeImage = new ImageView(new Image("daterangeicon.png"));
    dateRangeImage.setFitHeight(30);
    dateRangeImage.setFitWidth(30);
    Label fileNotFound = new Label("");

    Label reportsLabel = new Label("Get Reports:");
    Button farmReport = new Button("Farm", farmImage);
    Button annualReport = new Button("Annual", annualImage);
    Button monthlyReport = new Button("Monthly", monthImage);
    Button dateRangeReport = new Button("Date-Range", dateRangeImage);

    // Allow button to take user to farmReport scene
    farmReport.setOnAction(e -> {
      try {
        fileNotFound.setText("");
        setFarmReportScene(mainScene, primaryStage);
      } catch (FileNotFoundException fe) {
        fileNotFound.setText("File not found. Try again.");
      }
    });

    // Allow button to take user to annualReport scene
    annualReport.setOnAction(e -> {
      try {
        fileNotFound.setText("");
        setAnnualReportScene(mainScene, primaryStage);
      } catch (FileNotFoundException fe) {
        fileNotFound.setText("File not found. Try again.");
      }
    });

    // Allow button to take user to monthlyReport scene
    monthlyReport.setOnAction(e -> {
      try {
        fileNotFound.setText("");
        setMonthlyReportScene(mainScene, primaryStage);
      } catch (FileNotFoundException fe) {
        fileNotFound.setText("File not found. Try again.");
      }
    });

    // Allow button to take user to dateRangeReport scene
    dateRangeReport.setOnAction(e -> {
      try {
        fileNotFound.setText("");
        setDateRangeReportScene(mainScene, primaryStage);
      } catch (FileNotFoundException fe) {
        fileNotFound.setText("File not found. Try again.");
      }
    });

    // Display
    farmReport.setStyle("-fx-background-color: #00E8FF");
    farmReport.setOnMouseEntered(e -> farmReport.setStyle("-fx-background-color: #CD5BFF"));
    farmReport.setOnMouseExited(e -> farmReport.setStyle("-fx-background-color: #00E8FF"));
    annualReport.setStyle("-fx-background-color: #FFA600");
    annualReport.setOnMouseEntered(e -> annualReport.setStyle("-fx-background-color: #CD5BFF"));
    annualReport.setOnMouseExited(e -> annualReport.setStyle("-fx-background-color: #FFA600"));
    monthlyReport.setStyle("-fx-background-color: #FFFB00");
    monthlyReport.setOnMouseEntered(e -> monthlyReport.setStyle("-fx-background-color: #CD5BFF"));
    monthlyReport.setOnMouseExited(e -> monthlyReport.setStyle("-fx-background-color: #FFFB00"));
    dateRangeReport.setStyle("-fx-background-color: #59FF00");
    dateRangeReport
        .setOnMouseEntered(e -> dateRangeReport.setStyle("-fx-background-color: #CD5BFF"));
    dateRangeReport
        .setOnMouseExited(e -> dateRangeReport.setStyle("-fx-background-color: #59FF00"));

    HBox hbox = new HBox(farmReport, annualReport, monthlyReport, dateRangeReport);
    hbox.setSpacing(10);
    hbox.setAlignment(Pos.CENTER);
    VBox vbox = new VBox(fileNotFound, reportsLabel, hbox);
    vbox.setAlignment(Pos.CENTER);
    root.setBottom(vbox);
  }

  /**
   * Back button functionality for individual entered/deleted pieces of data. Once pressed, all of
   * the original data in file will be restored.
   * 
   * @author Jeffrey Li, Yash Hindka
   */
  private void backButtonAction() {
    if (deletedEntries.size() > 0) { // Adds back entries deleted by user
      File addDeletedEntries = new File("Deleted.txt"); // Creates file of list
      try {
        PrintWriter pw = new PrintWriter(addDeletedEntries);
        pw.write("year,farm_id,weight,% of total weight\n");

        for (String deleted : deletedEntries) {
          // add deleted entry back to backend
          pw.write(deleted + "\n");
        }
        pw.close();
        Main.manager.addNewData(addDeletedEntries);
        addDeletedEntries.delete();
        deletedEntries.clear();
      } catch (FileNotFoundException e1) {
        // if file wasn't found
      }
    }
    if (addedEntries.size() > 0) { // deletes the entries added by user
      for (String added : addedEntries) { // splits up data
        String[] addedParts = added.split(",");
        String[] dateParts = addedParts[0].split("-");
        // removes milk weight for each string in addedEntries
        for (Year year : Main.manager.getYears()) {
          try {
            if (year.getYearNumber() == Integer.parseInt(dateParts[0])) {
              Month specified = year.getMonth(Integer.parseInt(dateParts[1]) - 1);
              try {
                specified.removeMilkWeightForFarm(addedParts[1], Integer.parseInt(dateParts[2]),
                    Integer.parseInt(addedParts[2]));
              } catch (NumberFormatException e1) {
                // if data wasn't valid
              } catch (FarmNotFoundException e1) {
                // if farm not found
              }
            }
          } catch (NumberFormatException e) {
            // if data wasn't valid
          }
        }
      }
      addedEntries.clear();
    }
  }

  /**
   * Sets the farm report scene.
   * 
   * @author Yash Hindka, Moulik Mehta
   * 
   * @param mainScene    - current scene
   * @param primaryStage - current stage
   * @throws FileNotFoundException - if file wasn't found
   */
  private void setFarmReportScene(Scene mainScene, Stage primaryStage)
      throws FileNotFoundException {
    Label listLabel = new Label("date,farm_id,weight");
    VBox farmReportBox = new VBox(listLabel, getListOfData());

    // Allow user to return to main scene
    Button backButton = new Button("Back");
    backButton.setOnAction(e -> {
      primaryStage.setScene(mainScene);
      backButtonAction();
      File removeAddtoReport = new File("AddToReport.txt");
      removeAddtoReport.delete();
    });
    TextField farmReportIdPrompt = new TextField(); // Text field to enter FarmID
    farmReportIdPrompt.setPromptText("Enter Farm ID (For example: Farm 0)");
    TextField farmReportYearPrompt = new TextField(); // Text field to enter Year
    farmReportYearPrompt
        .setPromptText("Enter Year to retrieve data from. Type ALL to use all data");
    Button generateReport = new Button("Generate Report");
    Label inputException = new Label("");
    HBox backButtonNote = new HBox(backButton);
    HBox generateReportException = new HBox(generateReport, inputException);

    generateReport.setOnAction(e -> {// Functionality of Generate Report
      try {
        Report farmReport;
        // need to check if user wants data for certain year or all years
        if (farmReportYearPrompt.getText().trim().equals("ALL")) {
          farmReport = new Report(ReportTypes.FARM, farmReportIdPrompt.getText().trim(), 0, true);
        } else {
          farmReport = new Report(ReportTypes.FARM, farmReportIdPrompt.getText().trim(),
              Integer.parseInt(farmReportYearPrompt.getText().trim()), false);
        }
        File reportFile = farmReport.getReportData();
        Scanner scn = new Scanner(reportFile);
        // puts data in report into TableView
        TableView<FarmDataWithDate> table = createTableWithDate(farmReport.getFarmObservableList());
        Button backButton2 = new Button("Back");
        backButton2.setOnAction(e1 -> {// Back Button in Gen Report Scene
          primaryStage.setScene(mainScene);
          backButtonAction();
          File removeAddtoReport = new File("AddToReport.txt");
          removeAddtoReport.delete();
        });
        VBox reportBox = new VBox();
        reportBox.getChildren().addAll(table, backButton2);
        Scene reportScene = new Scene(reportBox, WINDOW_WIDTH, WINDOW_HEIGHT);
        // need to change primaryStage to display report
        primaryStage.setScene(reportScene);
      } catch (NumberFormatException e1) {
        inputException.setText("Year/FarmID is not in correct format. Try again.");
      } catch (FileNotFoundException e1) {
        inputException.setText("File is not found. Try again.");
      } catch (IOException e1) {
        inputException.setText("Unexpected Error. Try again.");
      }
    });

    // Display
    generateReport.setStyle("-fx-background-color: #00E8FF");
    generateReport.setOnMouseEntered(e -> generateReport.setStyle("-fx-background-color: #00D4FF"));
    generateReport.setOnMouseExited(e -> generateReport.setStyle("-fx-background-color: #00E8FF"));
    farmReportBox.getChildren().addAll(backButtonNote, farmReportIdPrompt, farmReportYearPrompt,
        generateReportException);
    Scene farmReportScene = new Scene(farmReportBox, WINDOW_WIDTH, WINDOW_HEIGHT);
    primaryStage.setScene(farmReportScene);

  }

  /**
   * Sets the Annual Report Scene. Check comments in setFarmReportScene for details.
   * 
   * @param mainScene    - current scene
   * @param primaryStage - primary stage
   * @throws FileNotFoundException - if file isn't found
   */
  private void setAnnualReportScene(Scene mainScene, Stage primaryStage)
      throws FileNotFoundException {
    Label listLabel = new Label("date,farm_id,weight");
    VBox annualReportBox = new VBox(listLabel, getListOfData());

    // Allow user to return to main scene
    Button backButton = new Button("Back");
    backButton.setOnAction(e -> {
      primaryStage.setScene(mainScene);
      backButtonAction();
      File removeAddtoReport = new File("AddToReport.txt");
      removeAddtoReport.delete();
    });
    TextField annualReportYearPrompt = new TextField(); // Text field to enter Year
    annualReportYearPrompt.setPromptText("Enter Year to retrieve data from.");
    Button generateReport = new Button("Generate Report");
    Label inputException = new Label("");
    HBox backButtonNote = new HBox(backButton);
    HBox generateReportException = new HBox(generateReport, inputException);

    generateReport.setOnAction(e -> {// Functionality of Generate Report
      try {
        Report annualReport = new Report(ReportTypes.ANNUAL,
            Integer.parseInt(annualReportYearPrompt.getText().trim()));
        File reportFile = annualReport.getReportData();
        Scanner scn = new Scanner(reportFile);
        TableView<FarmDataNoDate> table =
            createTableNoDateAnnualReport(annualReport.getAnnualObservableList());
        scn.close();
        Button backButton2 = new Button("Back");
        backButton2.setOnAction(e1 -> {// Back Button in Gen Report Scene
          primaryStage.setScene(mainScene);
          backButtonAction();
          File removeAddtoReport = new File("AddToReport.txt");
          removeAddtoReport.delete();
        });
        VBox reportBox = new VBox();
        reportBox.getChildren().addAll(table, backButton2);
        Scene reportScene = new Scene(reportBox, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(reportScene);
      } catch (NumberFormatException e1) {
        inputException.setText("Year is not in correct format. Try again.");
      } catch (FileNotFoundException e1) {
        inputException.setText("File is not found. Try again.");
      } catch (NullPointerException e1) {
        // if nothing selected
      } catch (IOException e1) {
        inputException.setText("Unexpected exception. Try again.");
      }
    });

    // Display
    generateReport.setStyle("-fx-background-color: #FFA600");
    generateReport.setOnMouseEntered(e -> generateReport.setStyle("-fx-background-color: #FF7C00"));
    generateReport.setOnMouseExited(e -> generateReport.setStyle("-fx-background-color: #FFA600"));
    annualReportBox.getChildren().addAll(backButtonNote, annualReportYearPrompt,
        generateReportException);
    Scene annualReportScene = new Scene(annualReportBox, WINDOW_WIDTH, WINDOW_HEIGHT);
    primaryStage.setScene(annualReportScene);
  }

  /**
   * Sets the Monthly Report Scene.
   * 
   * @param mainScene    - current scene
   * @param primaryStage - primary stage
   * @throws FileNotFoundException - if file isn't found
   */
  private void setMonthlyReportScene(Scene mainScene, Stage primaryStage)
      throws FileNotFoundException {
    Label listLabel = new Label("date,farm_id,weight");
    VBox monthlyReportBox = new VBox(listLabel, getListOfData());

    // Allow user to return to main scene
    Button backButton = new Button("Back");
    backButton.setOnAction(e -> {
      primaryStage.setScene(mainScene);
      backButtonAction();
      File removeAddtoReport = new File("AddToReport.txt");
      removeAddtoReport.delete();
    });
    TextField monthlyReportYearPrompt = new TextField(); // Text field to enter Year
    monthlyReportYearPrompt.setPromptText("Enter Year to retrieve data from.");
    TextField monthlyReportMonthPrompt = new TextField(); // Text field to enter Month
    monthlyReportMonthPrompt.setPromptText("Enter Month to retrieve data from.");
    Button generateReport = new Button("Generate Report");
    Label inputException = new Label("");
    HBox backButtonNote = new HBox(backButton);
    HBox generateReportException = new HBox(generateReport, inputException);

    generateReport.setOnAction(e -> {// Functionality of Generate Report
      try {
        Report monthlyReport = new Report(ReportTypes.MONTHLY,
            Integer.parseInt(monthlyReportYearPrompt.getText().trim()),
            Integer.parseInt(monthlyReportMonthPrompt.getText().trim()));
        if (Integer.parseInt(monthlyReportMonthPrompt.getText()) < 1
            || Integer.parseInt(monthlyReportMonthPrompt.getText()) > 12) {
          throw new NumberFormatException();
        }
        File reportFile = monthlyReport.getReportData();
        Scanner scn = new Scanner(reportFile);
        TableView<FarmDataNoDate> table =
            createTableNoDate(monthlyReport.getMonthlyObservableList());
        scn.close();
        Button backButton2 = new Button("Back");
        backButton2.setOnAction(e1 -> {// Back Button in Gen Report Scene
          primaryStage.setScene(mainScene);
          backButtonAction();
          File removeAddtoReport = new File("AddToReport.txt");
          removeAddtoReport.delete();
        });
        VBox reportBox = new VBox();
        reportBox.getChildren().addAll(table, backButton2);
        Scene reportScene = new Scene(reportBox, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(reportScene);
      } catch (NumberFormatException e1) {
        inputException.setText("Please input valid integers for Year and Month. Try again.");
      } catch (FileNotFoundException e1) {
        inputException.setText("File is not found. Try again.");
      } catch (NullPointerException e1) {
        // if nothing entered
      } catch (IOException e1) {
        inputException.setText("Unexpected exception. Try again.");
      }
    });

    // Display
    generateReport.setStyle("-fx-background-color: #FFFB00");
    generateReport.setOnMouseEntered(e -> generateReport.setStyle("-fx-background-color: #C9FF00"));
    generateReport.setOnMouseExited(e -> generateReport.setStyle("-fx-background-color: #FFFB00"));
    monthlyReportBox.getChildren().addAll(backButtonNote, monthlyReportYearPrompt,
        monthlyReportMonthPrompt, generateReportException);
    Scene monthlyReportScene = new Scene(monthlyReportBox, WINDOW_WIDTH, WINDOW_HEIGHT);
    primaryStage.setScene(monthlyReportScene);
  }

  /**
   * Sets the Date Range Report Scene.
   * 
   * @param mainScene    - current scene
   * @param primaryStage - primary stage
   * @throws FileNotFoundException - if file isn't found
   */
  private void setDateRangeReportScene(Scene mainScene, Stage primaryStage)
      throws FileNotFoundException {
    Label listLabel = new Label("date,farm_id,weight");
    VBox dateRangeReportBox = new VBox(listLabel, getListOfData());

    // Allow user to return to main scene
    Button backButton = new Button("Back");
    backButton.setOnAction(e -> {
      primaryStage.setScene(mainScene);
      backButtonAction();
      File removeAddtoReport = new File("AddToReport.txt");
      removeAddtoReport.delete();
    });
    TextField dateRangeReportStartPrompt = new TextField(); // Text field to enter Start Date
    dateRangeReportStartPrompt.setPromptText("Enter Start Date (yyyy-mm-dd)");
    TextField dateRangeReportEndPrompt = new TextField(); // Text field to enter End Date
    dateRangeReportEndPrompt.setPromptText("Enter End Date (yyyy-mm-dd)");
    Button generateReport = new Button("Generate Report");
    Label inputException = new Label("");
    HBox backButtonNote = new HBox(backButton);
    HBox generateReportException = new HBox(generateReport, inputException);

    generateReport.setOnAction(e -> { // Functionality of Generate Report
      try {
        Report dateRangeReport = new Report(ReportTypes.DATE_RANGE,
            dateRangeReportStartPrompt.getText(), dateRangeReportEndPrompt.getText());
        File reportFile = dateRangeReport.getReportData();
        Scanner scn = new Scanner(reportFile);
        TableView<FarmDataNoDate> table =
            createTableNoDate(dateRangeReport.getDateRangeObservableList());
        scn.close();
        Button backButton2 = new Button("Back");
        backButton2.setOnAction(e1 -> { // Back Button in Gen Report Scene
          primaryStage.setScene(mainScene);
          backButtonAction();
          File removeAddtoReport = new File("AddToReport.txt");
          removeAddtoReport.delete();
        });
        VBox reportBox = new VBox();
        reportBox.getChildren().addAll(table, backButton2);
        Scene reportScene = new Scene(reportBox, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(reportScene);
      } catch (NumberFormatException e1) {
        inputException.setText("Format is not correct.");
      } catch (FileNotFoundException e1) {
        inputException.setText("File is not found. Try again.");
      } catch (NullPointerException e1) {
        // if nothing is selected
      } catch (IOException e1) {
        inputException.setText("Unexpected exception. Try again.");
      } catch (ParseException e1) {
        inputException.setText("Date(s) are not correctly formatted (yyyy-mm-dd)");
      } catch (ArithmeticException e1) {
        inputException.setText("End Date cannot be before Start Date");
      }
    });

    // Display
    generateReport.setStyle("-fx-background-color: #59FF00");
    generateReport.setOnMouseEntered(e -> generateReport.setStyle("-fx-background-color: #53EE00"));
    generateReport.setOnMouseExited(e -> generateReport.setStyle("-fx-background-color: #59FF00"));
    dateRangeReportBox.getChildren().addAll(backButtonNote, dateRangeReportStartPrompt,
        dateRangeReportEndPrompt, generateReportException);
    Scene monthlyReportScene = new Scene(dateRangeReportBox, WINDOW_WIDTH, WINDOW_HEIGHT);
    primaryStage.setScene(monthlyReportScene);
  }

  /**
   * Creates a TableView with no Date entry.
   * 
   * @author Moulik Mehta
   * 
   * @param list - list of data
   * @return - the displayed table.
   */
  @SuppressWarnings("unchecked")
  private TableView<FarmDataNoDate> createTableNoDate(ObservableList<FarmDataNoDate> list) {
    TableColumn<FarmDataNoDate, String> farmIDColumn = new TableColumn<>("Farm ID");
    farmIDColumn.setMinWidth(200);
    farmIDColumn.setCellValueFactory(new PropertyValueFactory<>("farmID"));

    TableColumn<FarmDataNoDate, Integer> totalWeightColumn = new TableColumn<>("Total Weight");
    totalWeightColumn.setMinWidth(200);
    totalWeightColumn.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));

    TableColumn<FarmDataNoDate, String> percentageColumn = new TableColumn<>("Percent of Total");
    percentageColumn.setMinWidth(200);
    percentageColumn.setCellValueFactory(new PropertyValueFactory<>("percentageOfTotal"));

    TableView<FarmDataNoDate> table = new TableView<>();
    table.setItems(list);
    table.getColumns().addAll(farmIDColumn, totalWeightColumn, percentageColumn);

    return table;

  }

  /**
   * Creates a TableView with no Date for annual report.
   * 
   * @author Moulik Mehta
   * 
   * @param list - list of data
   * @return - the displayed table.
   */
  @SuppressWarnings("unchecked")
  private TableView<FarmDataNoDate> createTableNoDateAnnualReport(
      ObservableList<FarmDataNoDate> list) {

    // setup TableView
    TableColumn<FarmDataNoDate, String> farmIDColumn = new TableColumn<>("Farm ID");
    farmIDColumn.setMinWidth(200);
    farmIDColumn.setCellValueFactory(new PropertyValueFactory<>("farmID"));

    TableColumn<FarmDataNoDate, Integer> totalWeightColumn = new TableColumn<>("Total Weight");
    totalWeightColumn.setMinWidth(200);
    totalWeightColumn.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));

    TableColumn<FarmDataNoDate, String> percentageColumn = new TableColumn<>("Percent of Total");
    percentageColumn.setMinWidth(200);
    percentageColumn.setCellValueFactory(new PropertyValueFactory<>("percentageOfTotal"));

    // add columns for minimum, maximum, and average weights
    TableColumn<FarmDataNoDate, Integer> minWeightColumn =
        new TableColumn<>("Min Weight One Month");
    minWeightColumn.setMinWidth(200);
    minWeightColumn.setCellValueFactory(new PropertyValueFactory<>("minWeightOneMonth"));

    TableColumn<FarmDataNoDate, Integer> maxWeightColumn =
        new TableColumn<>("Max Weight One Month");
    maxWeightColumn.setMinWidth(200);
    maxWeightColumn.setCellValueFactory(new PropertyValueFactory<>("maxWeightOneMonth"));

    TableColumn<FarmDataNoDate, String> avgWeightColumn = new TableColumn<>("Avg Weight per Month");
    avgWeightColumn.setMinWidth(200);
    avgWeightColumn.setCellValueFactory(new PropertyValueFactory<>("avgWeightPerMonth"));

    TableView<FarmDataNoDate> table = new TableView<>();

    table.setItems(list);
    table.getColumns().addAll(farmIDColumn, totalWeightColumn, percentageColumn, minWeightColumn,
        maxWeightColumn, avgWeightColumn);

    return table;

  }

  /**
   * Creates a TableView with no Date entry.
   * 
   * @author Moulik Mehta
   * 
   * @param list - list of data
   * @return - the displayed table.
   */
  @SuppressWarnings("unchecked")
  private TableView<FarmDataWithDate> createTableWithDate(ObservableList<FarmDataWithDate> list) {

    TableColumn<FarmDataWithDate, String> dateColumn = new TableColumn<>("Month");
    dateColumn.setMinWidth(200);
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

    TableColumn<FarmDataWithDate, String> farmIDColumn = new TableColumn<>("Farm ID");
    farmIDColumn.setMinWidth(200);
    farmIDColumn.setCellValueFactory(new PropertyValueFactory<>("farmID"));

    TableColumn<FarmDataWithDate, Integer> totalWeightColumn = new TableColumn<>("Total Weight");
    totalWeightColumn.setMinWidth(200);
    totalWeightColumn.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));

    TableColumn<FarmDataWithDate, String> percentageColumn = new TableColumn<>("Percent of Total");
    percentageColumn.setMinWidth(200);
    percentageColumn.setCellValueFactory(new PropertyValueFactory<>("percentageOfTotal"));

    TableView<FarmDataWithDate> table = new TableView<>();

    table.setItems(list);

    table.getColumns().addAll(dateColumn, farmIDColumn, totalWeightColumn, percentageColumn);

    return table;

  }

  /**
   * Creates a VBox that contains the list of data read and buttons to edit, delete, and add data.
   * 
   * @author everyone
   * 
   * @return a VBox that contains a list of all data read from files, as well as options to edit,
   *         delete, and add.
   * @throws FileNotFoundException
   */
  private VBox getListOfData() throws FileNotFoundException {

    VBox reportListBox = new VBox();
    ListView<String> listOfEntries = new ListView<String>(); // A scene control element.
    ObservableList<String> dataEntries = manager.getListOfAllData(uploadedFileList);

    while (dataEntries.contains("date,farm_id,weight")) {
      dataEntries.remove("date,farm_id,weight");
    }

    File removedData = new File("RemovedErrorData.txt");
    Scanner scnr = new Scanner(removedData);
    while (scnr.hasNextLine()) {
      dataEntries.remove(scnr.nextLine());
    }
    scnr.close();
    listOfEntries.setItems(dataEntries);

    TextField entryTextField = new TextField();
    Button updateButton = new Button("Update");
    Button addButton = new Button("Add");
    Button deleteButton = new Button("Delete");
    Button exportButton = new Button("Export data to CSV file");
    Label userChanges = new Label(""); // displays user update/add/delete operations
    Label invalidPrompt = new Label("");
    Label indexOfSelectedItem = new Label();
    // When the user selects an element in the list, set the TextField entry to this
    // text.
    listOfEntries.setOnMouseClicked((e) -> {
      entryTextField.setText(listOfEntries.getSelectionModel().getSelectedItem());
      indexOfSelectedItem.setText(listOfEntries.getSelectionModel().getSelectedIndex() + "");
    });

    // Button allows the user to delete a selected entry.
    deleteButton.setOnAction(e -> {
      invalidPrompt.setText("");
      userChanges.setText("");
      if (entryTextField != null) {
        String selectedItem = entryTextField.getText();
        if (selectedItem != null) {
          selectedItem = selectedItem.trim(); // trims the selected item
          if (!(selectedItem == null) && !selectedItem.equals("")) {
            if (dataEntries.remove(selectedItem)) { // Remove it from the observable list.
              listOfEntries.setItems(dataEntries); // Make it so that the ListView is updated.
              entryTextField.setText(""); // Make the TextField not show anything anymore;
              userChanges.setText("Deleted " + selectedItem + " from the list.");
              // add deleted entry to deletedEntries
              deletedEntries.add(selectedItem);
              // remove deleted entry from backend data structures
              deleteFromBackend(selectedItem);
            } else {
              invalidPrompt.setText("This entry does not exist");
            }
          }
        }
      }
    });

    // Button allows the user to add a specified entry
    addButton.setOnAction(e -> {
      invalidPrompt.setText("");
      userChanges.setText("");
      if (entryTextField != null) {
        String addItem = entryTextField.getText();
        if (addItem != null) {
          addItem = addItem.trim();
          if (!(addItem == null) && !addItem.equals("")) {
            if (dataEntries.contains(addItem)) {
              invalidPrompt.setText("This entry is already in the manager!");
              return;
            }
            // add entry to backend
            boolean addedSuccessfully = false;
            try {
              addedSuccessfully = addToBackend(addItem);
            } catch (FileNotFoundException e1) {
              invalidPrompt.setText("File not found.");
            }
            if (addedSuccessfully) {
              addedEntries.add(addItem);
              dataEntries.add(addItem); // Add it to the observable list.
              listOfEntries.setItems(dataEntries); // Make it so that the ListView is updated.
              entryTextField.setText(""); // Make the TextField not show anything anymore;
              userChanges.setText("Added " + addItem + " to end of the list.");
            } else {
              invalidPrompt.setText(
                  "New data is not formatted correctly or entry on the same day already exists");
            }
          }
        }
      }
    });

    // Button allows the user to edit a selected entry.
    updateButton.setOnAction(e -> {
      invalidPrompt.setText("");
      userChanges.setText("");
      if (entryTextField != null) {
        String updatedItem = entryTextField.getText();
        if (updatedItem != null) {
          updatedItem = updatedItem.trim();
          if (updatedItem == null || updatedItem.equals("")) {
            return;
          }
          if (dataEntries.contains(updatedItem)) {
            invalidPrompt.setText("This entry is already in the manager!");
            return;
          }
          try {
            int index = Integer.parseInt(indexOfSelectedItem.getText());
            if (index < 0)
              throw new NumberFormatException();
            String delete = dataEntries.get(index);

            // to update an entry, we will first delete the entry from the backend and then add the
            // updated value to the backend

            // delete from backend
            deleteFromBackend(delete);

            // add updated entry to backend
            if (addToBackend(updatedItem)) {

              dataEntries.remove(index);
              addedEntries.add(updatedItem);

              deletedEntries.add(delete);
              dataEntries.add(index, updatedItem);

              userChanges.setText("Updated " + delete + " to " + updatedItem);
            } else {
              addToBackend(delete);
              invalidPrompt.setText(
                  "New data is not formatted correctly or entry on the same day already exists");
            }

          } catch (NumberFormatException e1) {
            invalidPrompt.setText("No entry was selected. Try again.");
          } catch (FileNotFoundException e1) {
            invalidPrompt.setText("File not found. Try again.");
          }
        }
      }
    });

    // Button that allows user to export results to file.
    exportButton.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Export data to");
      fileChooser.setInitialFileName("exportdata.csv");
      File selectedExport = fileChooser.showSaveDialog(null);

      if (selectedExport != null) {
        try {
          selectedExport.delete();
          selectedExport.createNewFile();
          PrintWriter writer = new PrintWriter(selectedExport);
          writer.print("date,farm_id,weight\n");
          dataEntries.forEach(e1 -> writer.print(e1 + "\n")); // write each data entry to external
                                                              // file
          writer.close();
        } catch (IOException e1) {

        }

      } else {
        return;
      }
    });

    // Display
    HBox changes = new HBox(entryTextField, updateButton, addButton, deleteButton, exportButton,
        userChanges, invalidPrompt);
    reportListBox.getChildren().addAll(listOfEntries, changes);

    return reportListBox;
  }

  /**
   * Deletes a piece of data from backend by finding the specified date and setting that milkweight
   * to 0
   * 
   * @author Yash Hindka
   * 
   * @param delete - data to be deleted
   */
  private void deleteFromBackend(String delete) {
    String[] selectedParts = delete.split(",");
    String[] dateParts = selectedParts[0].split("-"); // parse data
    for (Year year : Main.manager.getYears()) {

      if (year.getYearNumber() == Integer.parseInt(dateParts[0])) {
        Month specified = year.getMonth(Integer.parseInt(dateParts[1]) - 1);
        try { // removes from backend
          specified.removeMilkWeightForFarm(selectedParts[1], Integer.parseInt(dateParts[2]),
              Integer.parseInt(selectedParts[2]));
        } catch (NumberFormatException e1) {
          // if data wasn't formatted correctly
        } catch (FarmNotFoundException e1) {
          // if farm not found
        }
      }
    }

  }

  /**
   * Adds a piece of data to backend by adding the data to a file and then calling a FarmDataManager
   * method that adds data
   * 
   * @author Yash Hindka, Albert Men
   * 
   * @param add - data to be added
   */
  private boolean addToBackend(String add) throws FileNotFoundException {
    File addReport = new File("AddToReport.txt");
    PrintWriter printWriter = null;
    try {
      addReport.createNewFile();
      printWriter = new PrintWriter(addReport); // Can throw a FileNotFoundException.
      printWriter.write("date,farm_id,weight\n" + add); // Print the String result to output.txt.
    } catch (IOException e) {

    } finally {
      if (printWriter != null) {
        printWriter.close();
        if (Main.manager.addNewData(addReport)) { // if successfully adds a piece of data
          return true;
        } else {
          return false;
        }
      }
    }
    return false;
  }
}
