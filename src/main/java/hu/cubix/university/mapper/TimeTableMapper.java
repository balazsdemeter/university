package hu.cubix.university.mapper;

import hu.cubix.university.api.model.TimeTableDto;
import hu.cubix.university.model.TimeTable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimeTableMapper {
    public static List<TimeTableDto> mapToTimeTableDtos(Map<LocalDate, List<TimeTable>> map) {
        List<TimeTableDto> timeTableList = new ArrayList<>();
        map.forEach((date, timeTables) -> {
            timeTables.forEach(timeTable -> {
                TimeTableDto timeTableDto = new TimeTableDto();
                timeTableDto.setCourseName(timeTable.getCourse().getName());
                timeTableDto.setStartDate(date.atTime(timeTable.getStarTime()));
                timeTableDto.setEndDate(date.atTime(timeTable.getEndTime()));
                timeTableList.add(timeTableDto);
            });
        });
        return timeTableList;
    }

    public static List<hu.cubix.university.dto.TimeTableDto> mapToTimeTableDtos2(Map<LocalDate, List<TimeTable>> map) {
        List<hu.cubix.university.dto.TimeTableDto> timeTableList = new ArrayList<>();
        map.forEach((date, timeTables) -> {
            timeTables.forEach(timeTable -> {
                hu.cubix.university.dto.TimeTableDto timeTableDto = new hu.cubix.university.dto.TimeTableDto();
                timeTableDto.setCourseName(timeTable.getCourse().getName());
                timeTableDto.setStartDate(date.atTime(timeTable.getStarTime()));
                timeTableDto.setEndDate(date.atTime(timeTable.getEndTime()));
                timeTableList.add(timeTableDto);
            });
        });
        return timeTableList;
    }
}
