package hu.cubix.university.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private Integer studentId;
    private Integer amount;
}