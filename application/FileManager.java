package application;

/**
 * Defines required operations to read/write input/output files
 *
 */
public class FileManager {
  public String inputFile; // The file we read from.
  public String outputFile; // The file we provide the output to.
  
  /**
   * Reads the input file.
   * 
   * @return true if we are able to read the given file, false otherwise.
   */
  public boolean readFile() {
	  return false;
  }
  
  /**
   * Writes to the output file. 
   * 
   * @param This would depend on the type of report we need.
   * @return true if we are able to write to the output file, and false otherwise.
   */
  public boolean writeToFile() {
	  //Would we add helper methods for each type of report?
    return false;
  }
  
  /**
   * Gets the content of the input file in a String.
   * 
   * @return String, the contents of the input file.
   */
  public String getFileContents() {
    return null;
  }
}
