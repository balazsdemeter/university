package hu.cubix.university.repository;

import hu.cubix.university.model.Course;
import hu.cubix.university.model.QCourse;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends
        JpaRepository<Course, Integer>,
        JpaSpecificationExecutor<Course>,
        QuerydslPredicateExecutor<Course>,
        QuerydslBinderCustomizer<QCourse> {

    @EntityGraph(attributePaths = {"teachers", "students"})
    Optional<Course> findById(Integer id);

    @Override
    default void customize(QuerydslBindings bindings, QCourse root) {
        bindings.bind(root.name).first((path, value) -> path.startsWithIgnoreCase(value));
        bindings.bind(root.teachers.any().name).first((path, value) -> path.startsWithIgnoreCase(value));
        bindings.bind(root.students.any().semester).all((path, values) -> {
            if (values.size() != 2) {
                return Optional.empty();
            }

            Iterator<? extends Integer> iterator = values.iterator();
            Integer from = iterator.next();
            Integer to = iterator.next();

            return Optional.of(path.between(from, to));
        });
    }

    @EntityGraph(attributePaths = {"students"})
    @Query("SELECT c from Course c where c.id in :ids")
    List<Course> findAllWithStudentsById(List<Integer> ids);

    @EntityGraph(attributePaths = {"teachers"})
    @Query("SELECT c from Course c where c.id in :ids")
    List<Course> findAllWithTeachersById(List<Integer> ids, Sort sort);
}