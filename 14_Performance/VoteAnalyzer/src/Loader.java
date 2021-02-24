import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import model.Voter;
import model.WorkTime;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Loader {

  private static SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
  private static SimpleDateFormat visitDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

  private static HashMap<Short, WorkTime> voteStationWorkTimes = new HashMap<>();
  private static HashMap<Voter, Short> voterCounts = new HashMap<>();

  public static void main(String[] args) throws Exception {
    long usageMem = getUsageRAM();
    String fileName = "14_Performance/VoteAnalyzer/res/data-18M.xml";
    StringBuilder results = new StringBuilder();

    //DOM-parse
//    parseFile(fileName);
//
//    //Printing results
//    results.append("Voting station work times: \n");
//    for (short votingStation : voteStationWorkTimes.keySet()) {
//      WorkTime workTime = voteStationWorkTimes.get(votingStation);
//      results.append("\t").append(votingStation).append(" - ").append(workTime).append("\n");
//    }
//
//    results.append("Duplicated voters: \n");
//    for (Voter voter : voterCounts.keySet()) {
//      short count = voterCounts.get(voter);
//      if (count > 1) {
//        results.append("\t").append(voter).append(" - ").append(count).append("\n");
//      }
//    }

    //SAX-parse
    SAXParserFactory saxFactory = SAXParserFactory.newInstance();
    SAXParser saxParser = saxFactory.newSAXParser();
    XMLHandler saxHandler = new XMLHandler();

    saxParser.parse(new File(fileName), saxHandler);

    saxHandler.printVoteStationWorkTimes();
    saxHandler.printDuplicatedVoters();

    results.append("==============================================================\n")
        .append("Usage RAM: ").append((getUsageRAM() - usageMem) / 1000000).append(" Mb");
    System.out.println(results);
  }

  private static void parseFile(String fileName) throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(new File(fileName));

    findEqualVoters(doc);
    fixWorkTimes(doc);
  }

  private static void findEqualVoters(Document doc) throws Exception {
    NodeList voters = doc.getElementsByTagName("voter");
    int votersCount = voters.getLength();
    for (int i = 0; i < votersCount; i++) {
      Node node = voters.item(i);
      NamedNodeMap attributes = node.getAttributes();

      String name = attributes.getNamedItem("name").getNodeValue();
      long birthDay = birthDayFormat.parse(attributes.getNamedItem("birthDay").getNodeValue())
          .getTime();

      Voter voter = new Voter(name, birthDay);
      short count = 1;
      if (voterCounts.get(voter) != null) {
        count += voterCounts.get(voter);
      }
      voterCounts.put(voter, count);
    }
  }

  private static void fixWorkTimes(Document doc) throws Exception {
    NodeList visits = doc.getElementsByTagName("visit");
    int visitCount = visits.getLength();
    for (int i = 0; i < visitCount; i++) {
      Node node = visits.item(i);
      NamedNodeMap attributes = node.getAttributes();

      short station = Short.parseShort(attributes.getNamedItem("station").getNodeValue());
      long time = visitDateFormat.parse(attributes.getNamedItem("time").getNodeValue()).getTime();
      WorkTime workTime = voteStationWorkTimes.get(station);
      if (workTime == null) {
        workTime = new WorkTime();
        voteStationWorkTimes.put(station, workTime);
      }
      workTime.addVisitTime(time);
    }
  }

  private static long getUsageRAM() {

    return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
  }
}