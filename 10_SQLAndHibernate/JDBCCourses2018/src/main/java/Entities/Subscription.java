package Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "subscriptions")
@IdClass(Subscription.Key.class)
public class Subscription {

  @Id
  @ManyToOne(cascade = CascadeType.ALL)
  private Student student;

  @Id
  @Column(name = "course_id")
  private int courseId;

  @Column(name = "subscription_date")
  private Date subscriptionDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Key implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    private Student student;

    @Column(name = "course_id")
    private int courseId;
  }
}
