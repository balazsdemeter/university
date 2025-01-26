package hu.cubix.university.model;

import hu.cubix.university.enums.SemesterEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity
@Audited
public class Semester {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int id;

    @Enumerated(value= EnumType.STRING)
    private SemesterEnum semester;

    private LocalDate startDate;

    private int length;

    @OneToMany
    @JoinColumn(name = "semester_id")
    private Set<TimeTable> timeTables;
}