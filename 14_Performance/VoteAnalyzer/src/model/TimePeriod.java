package model;

import java.text.SimpleDateFormat;

public class TimePeriod implements Comparable<TimePeriod> {

  private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyy.MM.dd");
  private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

  private long from;
  private long to;

  /**
   * Time period within one day
   *
   * @param from
   * @param to
   */
  public TimePeriod(long from, long to) {
    this.from = from;
    this.to = to;
    if (!isSameDays(from, to)) {
      throw new IllegalArgumentException("Dates 'from' and 'to' must be within ONE day!");
    }
  }

  public void appendTime(long visitTime) {
    if (!isSameDays(from, visitTime)) {
      throw new IllegalArgumentException(
          "Visit time must be within the same day as the current TimePeriod!");
    }
    if (visitTime < from) {
      from = visitTime;
    }
    if (visitTime > to) {
      to = visitTime;
    }
  }

  public String toString() {
    return new StringBuilder()
        .append(DAY_FORMAT.format(from)).append(" ")
        .append(TIME_FORMAT.format(from)).append("-")
        .append(TIME_FORMAT.format(to)).toString();
  }

  @Override
  public int compareTo(TimePeriod period) {
    return DAY_FORMAT.format(from).compareTo(DAY_FORMAT.format(period.from));
  }

  private boolean isSameDays(long dateA, long dateB) {
    return DAY_FORMAT.format(dateA).equals(DAY_FORMAT.format(dateB));
  }
}
