import java.io.File;
import java.sql.SQLException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Loader {

  public static void main(String[] args) {
    String fileName = "14_Performance/VoteAnalyzer/res/data-1572M.xml";

    long start = System.currentTimeMillis();
    parseFile(fileName);
    System.out.println("Parsing duration: " + (System.currentTimeMillis() - start) + " ms");

    printVoteStationWorkTimes();
    printDuplicatedVoters();
  }

  private static void parseFile(String fileName) {
    try {
      SAXParserFactory saxFactory = SAXParserFactory.newInstance();
      SAXParser saxParser = saxFactory.newSAXParser();
      XMLHandler saxHandler = new XMLHandler();

      saxParser.parse(new File(fileName), saxHandler);
      DBConnection.executeMultiInsert();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void printDuplicatedVoters() {
    try {
      System.out.println("Duplicated voters:");
      DBConnection.printVoterCounts();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void printVoteStationWorkTimes() {
    try {
      System.out.println("Voting station work times:");
      DBConnection.printStationWorkTimeCounts();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}