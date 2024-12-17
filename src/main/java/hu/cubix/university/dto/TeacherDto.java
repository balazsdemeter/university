package hu.cubix.university.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TeacherDto {
    private Integer id;

    private String name;

    private LocalDate birthDate;

    private List<CourseDto> courses;
}
