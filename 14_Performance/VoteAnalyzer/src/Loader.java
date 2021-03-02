import java.io.File;
import java.sql.SQLException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Loader {

  private static final String FILE_NAME = "14_Performance/VoteAnalyzer/res/data-1572M.xml";

  public static void main(String[] args) {

    long start = System.currentTimeMillis();
    startDB();
    System.out.println("InitDB duration: "
        + (System.currentTimeMillis() - start) + " ms");

    start = System.currentTimeMillis();
    parseFile(FILE_NAME);
    System.out.println("Parsing duration: "
        + (System.currentTimeMillis() - start) + " ms");

    start = System.currentTimeMillis();
    indexingVoters();
    System.out.println("Voters indexing duration: "
        + (System.currentTimeMillis() - start) + " ms");

    start = System.currentTimeMillis();
    createResultTables();
    System.out.println("Result tables getting duration: "
        + (System.currentTimeMillis() - start) + " ms");

    printVoteStationWorkTimes();
    printDuplicatedVoters();
  }

  private static void startDB() {
    try {
      DBConnection.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
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

  private static void indexingVoters() {
    try {
      DBConnection.createIndexVoter();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void createResultTables() {
    try {
      DBConnection.insertDuplicateVoters();
      DBConnection.insertStationsWorkTimes();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void printDuplicatedVoters() {
    try {
      System.out.println("\nDuplicated voters:");
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