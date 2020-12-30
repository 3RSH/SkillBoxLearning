package Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "purchaselist")
public class Purchase {

  @EmbeddedId
  private Key id;

  @Column(name = "student_name", insertable = false, updatable = false)
  @JoinTable(name = "students"
      , inverseJoinColumns = {@JoinColumn(name = "student_id")})
  private String studentName;

  @Column(name = "course_name", insertable = false, updatable = false)
  @JoinTable(name = "courses"
      , inverseJoinColumns = {@JoinColumn(name = "course_id")})
  private String courseName;

  private int price;

  @Column(name = "subscription_date")
  private Date subscriptionDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Embeddable
  public static class Key implements Serializable {

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "course_name")
    private String courseName;
  }

}
