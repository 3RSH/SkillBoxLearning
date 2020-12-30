package Entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "linkedpurchaselist")
public class LinkedPurchase {

  @EmbeddedId
  private Key id;

  @Column(name = "student_id", insertable = false, updatable = false)
  private int studentId;

  @Column(name = "course_id", insertable = false, updatable = false)
  private int courseId;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Embeddable
  public static class Key implements Serializable {

    @Column(name = "student_id")
    private int studentId;

    @Column(name = "course_id")
    private int courseId;
  }
}
