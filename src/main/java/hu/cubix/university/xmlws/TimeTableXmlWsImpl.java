package hu.cubix.university.xmlws;

import hu.cubix.university.mapper.TimeTableMapper;
import hu.cubix.university.model.TimeTable;
import hu.cubix.university.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TimeTableXmlWsImpl implements TimeTableXmlWs {
    private final TimeTableService timeTableService;

    @Override
    public List<hu.cubix.university.dto.TimeTableDto> getTimeTableForStudent(String studentName, Integer weekNumber,
                                                     Integer weekNumberFrom, Integer weekNumberTo, String semester) {
        Map<LocalDate, List<TimeTable>> map = timeTableService.getAllByStudent(studentName, null, weekNumber, weekNumberFrom, weekNumberTo, semester);

        return TimeTableMapper.mapToTimeTableDtos2(map);
    }
}