package hu.cubix.university.repository;

import hu.cubix.university.model.Teacher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    @EntityGraph(attributePaths = {"courses"})
    Optional<Teacher> findById(Integer id);
}