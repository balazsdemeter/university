package hu.cubix.university.mapper;

import hu.cubix.university.dto.CourseDto;
import hu.cubix.university.dto.TeacherDto;
import hu.cubix.university.model.Course;
import hu.cubix.university.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    TeacherDto teacherToDto(Teacher teacher);

    Teacher dtoToTeacher(TeacherDto teacherDto);

    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "students", ignore = true)
    CourseDto courseToCourseDto(Course course);
}
