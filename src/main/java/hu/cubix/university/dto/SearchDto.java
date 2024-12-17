package hu.cubix.university.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    private String courseName;
    private Integer courseId;
    private String teacherName;
    private Integer studentId;
    private Integer semesterFrom;
    private Integer semesterTo;
}
