package hu.cubix.university.mapper;

import hu.cubix.university.dto.CourseDto;
import hu.cubix.university.dto.StudentDto;
import hu.cubix.university.model.Course;
import hu.cubix.university.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentDto studentToDto(Student student);

    Student dtoToStudent(StudentDto studentDto);

    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "students", ignore = true)
    CourseDto courseToCourseDto(Course course);
}