package hu.cubix.university.mapper;

import hu.cubix.university.dto.CourseDto;
import hu.cubix.university.dto.StudentDto;
import hu.cubix.university.dto.TeacherDto;
import hu.cubix.university.model.Course;
import hu.cubix.university.model.Student;
import hu.cubix.university.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Named("Summary")
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "teachers", ignore = true)
    CourseDto courseSummaryToDto(Course course);

    CourseDto courseToDto(Course course);

    Course dtoToCourse(CourseDto courseDto);

    @Mapping(target = "courses", ignore = true)
    StudentDto studentToStudentDto(Student student);

    @Mapping(target = "courses", ignore = true)
    TeacherDto teacherToTeacherDto(Teacher teacher);
}
