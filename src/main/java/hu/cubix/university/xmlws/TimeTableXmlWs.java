package hu.cubix.university.xmlws;

import jakarta.jws.WebService;

import java.util.List;

@WebService
public interface TimeTableXmlWs {
    List<hu.cubix.university.dto.TimeTableDto> getTimeTableForStudent(String studentName, Integer weekNumber, Integer weekNumberFrom, Integer weekNumberTo, String semester);
}