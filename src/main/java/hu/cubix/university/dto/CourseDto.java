package hu.cubix.university.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CourseDto {
    private Integer id;

    private String name;

    private List<StudentDto> students;

    private List<TeacherDto> teachers;
}
