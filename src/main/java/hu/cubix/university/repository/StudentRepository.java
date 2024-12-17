package hu.cubix.university.repository;

import hu.cubix.university.model.Student;
import hu.cubix.university.model.Teacher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    @EntityGraph(attributePaths = {"courses"})
    Optional<Student> findById(Integer id);
}