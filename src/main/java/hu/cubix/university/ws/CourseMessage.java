package hu.cubix.university.ws;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CourseMessage {
    private String courseName;
    private LocalDateTime dateTime;
    private String message;
}