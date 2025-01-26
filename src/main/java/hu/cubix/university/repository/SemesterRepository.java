package hu.cubix.university.repository;

import hu.cubix.university.enums.SemesterEnum;
import hu.cubix.university.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semester, Integer>  {
    List<Semester> findSemesterBySemester(SemesterEnum semester);
}