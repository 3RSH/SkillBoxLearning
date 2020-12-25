package Entities;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "students")
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  private int age;

  @Column(name = "registration_date")
  private Date registrationDate;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "subscriptions"
      , inverseJoinColumns = {@JoinColumn(name = "course_id")
      , @JoinColumn(name = "student_id")})
  private List<Subscription> subscriptions;
}
