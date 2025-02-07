package hu.cubix.university.web;

import hu.cubix.university.api.TimeTableControllerApi;
import hu.cubix.university.api.model.TimeTableDto;
import hu.cubix.university.mapper.TimeTableMapper;
import hu.cubix.university.model.TimeTable;
import hu.cubix.university.service.SchoolDaySwapService;
import hu.cubix.university.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class TimeTableController implements TimeTableControllerApi {

    private final SchoolDaySwapService schoolDaySwapService;
    private final TimeTableService timeTableService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return TimeTableControllerApi.super.getRequest();
    }

    @Override
    public ResponseEntity<Void> registerDayOff(LocalDate dayOffDate, LocalDate swappedDate) {
        schoolDaySwapService.registerDayOff(dayOffDate, swappedDate);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<TimeTableDto>> getTimeTableForTeacher(String teacherName, LocalDate courseDate, Integer weekNumber, Integer weekNumberFrom, Integer weekNumberTo, String semester) {
        Map<LocalDate, List<TimeTable>> map = timeTableService.getAllByTeacher(teacherName, courseDate, weekNumber, weekNumberFrom, weekNumberTo, semester);

        if (map == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(TimeTableMapper.mapToTimeTableDtos(map));
    }

    @Override
    public ResponseEntity<List<TimeTableDto>> getTimeTableForStudent(String studentName, LocalDate courseDate, Integer weekNumber, Integer weekNumberFrom, Integer weekNumberTo, String semester) {
        Map<LocalDate, List<TimeTable>> map = timeTableService.getAllByStudent(studentName, courseDate, weekNumber, weekNumberFrom, weekNumberTo, semester);

        if (map == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(TimeTableMapper.mapToTimeTableDtos(map));
    }
}