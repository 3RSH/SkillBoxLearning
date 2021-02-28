import java.sql.SQLException;
import model.Voter;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {

  private Voter voter;
  private int counter = 0;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    try {
      if (qName.equals("voter") && voter == null) {
        voter = new Voter(attributes.getValue("name")
            , attributes.getValue("birthDay"));

      } else if (qName.equals("visit") && voter != null) {
        counter++;

        DBConnection.countVoter(voter.getName(), voter.getBirthDay());
        DBConnection.countWorkTimeStation(Short.parseShort(attributes.getValue("station"))
            , attributes.getValue("time"));

        if (counter == 10000) {
          DBConnection.executeMultiInsert();
          counter = 0;
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
