package hu.cubix.university.service;

import hu.cubix.university.model.Course;
import hu.cubix.university.model.Student;
import hu.cubix.university.model.Teacher;
import hu.cubix.university.repository.CourseRepository;
import hu.cubix.university.repository.StudentRepository;
import hu.cubix.university.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class InitDbService {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public void deleteDb() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
        studentRepository.deleteAll();
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
    }
}
