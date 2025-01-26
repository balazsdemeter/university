package hu.cubix.university.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity
@Audited
public class TimeTable {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne
    private Course course;

    private LocalTime starTime;

    private LocalTime endTime;

    private DayOfWeek dayOfWeek;

    @ManyToOne
    private Semester semester;
}