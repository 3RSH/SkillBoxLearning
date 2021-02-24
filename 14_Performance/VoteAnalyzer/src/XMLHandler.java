import java.text.SimpleDateFormat;
import java.util.HashMap;
import model.Voter;
import model.WorkTime;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {

  private static final SimpleDateFormat BIRTH_DAY_FORMAT = new SimpleDateFormat("yyyy.MM.dd");
  private static final SimpleDateFormat VISIT_DATE_FORMAT
      = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

  private final HashMap<Voter, Short> voterCounts;
  private final HashMap<Short, WorkTime> voteStationWorkTimes;
  private Voter voter;

  public XMLHandler() {

    voterCounts = new HashMap<>();
    voteStationWorkTimes = new HashMap<>();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {

    try {
      if (qName.equals("voter") && voter == null) {

        long birthDay = BIRTH_DAY_FORMAT.parse(attributes.getValue("birthDay")).getTime();
        voter = new Voter(attributes.getValue("name"), birthDay);

      } else if (qName.equals("visit") && voter != null) {

        short count = voterCounts.getOrDefault(voter, (short) 0);
        voterCounts.put(voter, ++count);

        short station = Short.parseShort(attributes.getValue("station"));
        long visitDate = VISIT_DATE_FORMAT.parse(attributes.getValue("time")).getTime();
        WorkTime workTime = voteStationWorkTimes.get(station);

        if (workTime == null) {

          workTime = new WorkTime();
          voteStationWorkTimes.put(station, workTime);
        }

        workTime.addVisitTime(visitDate);
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

    StringBuilder output = new StringBuilder().append("Voting station work times: \n");

    for (short votingStation : voteStationWorkTimes.keySet()) {

      WorkTime workTime = voteStationWorkTimes.get(votingStation);
      output.append("\t").append(votingStation).append(" - ").append(workTime).append("\n");
    }

    System.out.print(output);
  }


  public void printDuplicatedVoters() {

    StringBuilder output = new StringBuilder().append("Duplicated voters: \n");

    for (Voter voter : voterCounts.keySet()) {

      short count = voterCounts.get(voter);

      if (count > 1) {

        output.append("\t").append(voter.toString()).append(" - ").append(count).append("\n");
      }
    }

    System.out.print(output);
  }
}
