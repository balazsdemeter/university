package hu.cubix.university.repository;

import hu.cubix.university.model.Course;
import hu.cubix.university.model.Semester;
import hu.cubix.university.model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface TimeTableRepository extends JpaRepository<TimeTable, Integer> {
    List<TimeTable> findTimeTableByDayOfWeekAndCourseInAndSemester(DayOfWeek dayOfWeek, Set<Course> courses, Semester semester);
}