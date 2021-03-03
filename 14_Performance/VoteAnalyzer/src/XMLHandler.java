import java.sql.SQLException;
import model.Voter;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {

  private final long startTime = System.currentTimeMillis();

  private Voter voter;
  private int counter;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    try {
      if (qName.equals("voter") && voter == null) {
        voter = new Voter(attributes.getValue("name")
            , attributes.getValue("birthDay"));

      } else if (qName.equals("visit") && voter != null) {
        counter++;
        DBConnection.countVoter(voter.getName(), voter.getBirthDay());
        DBConnection.countStationVisit(Short.parseShort(attributes.getValue("station"))
            , attributes.getValue("time"));

        if (counter % 100000 == 0) {
          System.out.printf("%d - %.1f s%n", counter,
              (System.currentTimeMillis() - startTime) / 1000.0);
          DBConnection.executeMultiInsert();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("voter")) {
      voter = null;
    }
  }
}