package model;

import java.text.SimpleDateFormat;

public class Voter {

  private String name;
  private long birthDay;

  public Voter(String name, long birthDay) {
    this.name = name;
    this.birthDay = birthDay;
  }

  @Override
  public boolean equals(Object obj) {
    if (!this.getClass().equals(obj.getClass())) {
      return false;
    }

    Voter voter = (Voter) obj;
    return name.equals(voter.name) && (birthDay == voter.birthDay);
  }

  @Override
  public int hashCode() {
    return name.hashCode() + Long.hashCode(birthDay);
  }

  public String toString() {
    SimpleDateFormat dayFormat = new SimpleDateFormat(" (yyyy.MM.dd)");
    return name + dayFormat.format(birthDay);
  }

  public String getName() {
    return name;
  }

  public long getBirthDay() {
    return birthDay;
  }
}
