package hu.cubix.university.service;

import hu.cubix.university.enums.SemesterEnum;
import hu.cubix.university.model.Course;
import hu.cubix.university.model.Semester;
import hu.cubix.university.model.Student;
import hu.cubix.university.model.Teacher;
import hu.cubix.university.model.TimeTable;
import hu.cubix.university.model.UniversityUser;
import hu.cubix.university.repository.CourseRepository;
import hu.cubix.university.repository.SemesterRepository;
import hu.cubix.university.repository.StudentRepository;
import hu.cubix.university.repository.TeacherRepository;
import hu.cubix.university.repository.TimeTableRepository;
import hu.cubix.university.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class InitDbService {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final TimeTableRepository timeTableRepository;
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SemesterRepository semesterRepository;

    @Transactional
    public void deleteDb() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
        studentRepository.deleteAll();
        timeTableRepository.deleteAll();
    }

    @Transactional
    public void deleteAudTables() {
        jdbcTemplate.update("DELETE FROM course_student_aud");
        jdbcTemplate.update("DELETE FROM course_teacher_aud");
        jdbcTemplate.update("DELETE FROM student_aud");
        jdbcTemplate.update("DELETE FROM teacher_aud");
        jdbcTemplate.update("DELETE FROM course_aud");
    }

    @Transactional
    public void addInitData() {
        Course test1 = courseRepository.save(Course.builder().name("TEST_1").build());
        Course test2 = courseRepository.save(Course.builder().name("TEST_2").build());

        Set<Course> courses1 = new HashSet<>();
        courses1.add(test1);
        teacherRepository.save(Teacher.builder()
                .birthDate(LocalDate.now())
                .name("teacher_1")
                .courses(courses1)
                .build()
        );

        studentRepository.save(Student.builder()
                .birthDate(LocalDate.now())
                .semester(1)
                .courses(courses1)
                .name("student_1")
                .build());

        Set<Course> courses2 = new HashSet<>();
        courses2.add(test2);
        studentRepository.save(Student.builder()
                .birthDate(LocalDate.now())
                .semester(9)
                .courses(courses2)
                .name("student_2")
                .build());

        List<Semester> semesterBySemester = semesterRepository.findSemesterBySemester(SemesterEnum.SPRING);

        TimeTable timeTable1 = new TimeTable();
        timeTable1.setCourse(test1);
        timeTable1.setStarTime(LocalTime.of(8, 0));
        timeTable1.setEndTime(LocalTime.of(10, 0));
        timeTable1.setDayOfWeek(DayOfWeek.MONDAY);
        timeTable1.setSemester(semesterBySemester.get(0));
        timeTableRepository.save(timeTable1);

        TimeTable timeTable2 = new TimeTable();
        timeTable2.setCourse(test2);
        timeTable2.setStarTime(LocalTime.of(14, 0));
        timeTable2.setEndTime(LocalTime.of(16, 0));
        timeTable2.setDayOfWeek(DayOfWeek.THURSDAY);
        timeTable2.setSemester(semesterBySemester.get(0));
        timeTableRepository.save(timeTable2);

        createUsersIfNeeded();
    }

    @Transactional
    public void createUsersIfNeeded() {
        if (!userRepository.existsById("admin")) {
            userRepository.save(new UniversityUser("admin", passwordEncoder.encode("pass"), Set.of("admin", "user"), null, null));
        }

        if (!userRepository.existsById("user")) {
            userRepository.save(new UniversityUser("user", passwordEncoder.encode("pass"), Set.of("user"), null, null));
        }
    }
}
