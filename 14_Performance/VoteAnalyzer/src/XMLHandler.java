import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import model.Voter;
import model.WorkTime;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {

  private static final SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
  private static final SimpleDateFormat visitDateFormat
      = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

  private final HashMap<Voter, Integer> voterCounts;
  private final HashMap<Integer, WorkTime> voteStationWorkTimes;
  private Voter voter;

  public XMLHandler() {

    voterCounts = new HashMap<>();
    voteStationWorkTimes = new HashMap<>();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {

    try {
      if (qName.equals("voter") && voter == null) {

        Date birthDay = birthDayFormat.parse(attributes.getValue("birthDay"));
        voter = new Voter(attributes.getValue("name"), birthDay);

      } else if (qName.equals("visit") && voter != null) {

        int count = voterCounts.getOrDefault(voter, 0);
        voterCounts.put(voter, count + 1);

        Integer station = Integer.parseInt(attributes.getValue("station"));
        Date visitDate = visitDateFormat.parse(attributes.getValue("time"));
        WorkTime workTime = voteStationWorkTimes.get(station);

        if (workTime == null) {

          workTime = new WorkTime();
          voteStationWorkTimes.put(station, workTime);
        }

        workTime.addVisitTime(visitDate.getTime());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {

    if (qName.equals("voter")) {

      voter = null;
    }
  }


  public void printVoteStationWorkTimes() {

    System.out.println("Voting station work times: ");

    for (Integer votingStation : voteStationWorkTimes.keySet()) {

      WorkTime workTime = voteStationWorkTimes.get(votingStation);
      System.out.println("\t" + votingStation + " - " + workTime);
    }
  }


  public void printDuplicatedVoters() {

    System.out.println("Duplicated voters: ");

    for (Voter voter : voterCounts.keySet()) {

      int count = voterCounts.get(voter);

      if (count > 1) {

        System.out.println("\t" + voter.toString() + " - " + count);
      }
    }
  }
}
