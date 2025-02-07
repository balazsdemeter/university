package hu.cubix.university.dto;

import hu.cubix.university.xmlws.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@XmlRootElement(name = "timeTableDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class TimeTableDto {
    private String courseName;

    @XmlElement(name = "startDate")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime startDate;

    @XmlElement(name = "endDate")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime endDate;
}