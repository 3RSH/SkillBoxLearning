package Entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "courses")
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  private int duration;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "enum")
  private CourseType type;

  private String description;

  @ManyToOne(cascade = CascadeType.ALL)
  private Teacher teacher;

  @Column(name = "students_count")
  private int studentsCount;

  private int price;

  @Column(name = "price_per_hour")
  private float pricePerHour;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "subscriptions"
      , inverseJoinColumns = {@JoinColumn(name = "course_id")
      , @JoinColumn(name = "student_id")})
  private List<Subscription> subscriptions;

  public enum CourseType {
    DESIGN,
    PROGRAMMING,
    MARKETING,
    MANAGEMENT,
    BUSINESS
  }
}
