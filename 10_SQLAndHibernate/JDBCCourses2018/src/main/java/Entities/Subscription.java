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
  private Student student;

  @Id
  private Course course;

  @Column(name = "subscription_date")
  private Date subscriptionDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Key implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    private Student student;

    @ManyToOne(cascade = CascadeType.ALL)
    private Course course;
  }
}
