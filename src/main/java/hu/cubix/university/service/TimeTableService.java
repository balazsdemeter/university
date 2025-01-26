package hu.cubix.university.service;

import hu.cubix.university.model.Course;
import hu.cubix.university.model.SchoolDaySwap;
import hu.cubix.university.model.Semester;
import hu.cubix.university.model.Student;
import hu.cubix.university.model.Teacher;
import hu.cubix.university.model.TimeTable;
import hu.cubix.university.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class TimeTableService {
    private final TimeTableRepository timeTableRepository;
    private final SemesterService semesterService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final SchoolDaySwapService schoolDaySwapService;

    private int DAYS_IN_WEEK = 7;

    public void addCourseToTimeTable(Course course, LocalTime startTime, LocalTime endTime, String dayOfWeek, String semester) {
        TimeTable timeTable = new TimeTable();
        timeTable.setCourse(course);
        timeTable.setStarTime(startTime);
        timeTable.setEndTime(endTime);
        timeTable.setDayOfWeek(DayOfWeek.valueOf(dayOfWeek));
        timeTable.setSemester(semesterService.findSemester(semester));
        timeTableRepository.save(timeTable);
    }

    @Cacheable("getAllByTeacher")
    public Map<LocalDate, List<TimeTable>> getAllByTeacher(String teacherName, LocalDate courseDate, Integer weekNumber,
                                                           Integer weekNumberFrom, Integer weekNumberTo, String semesterName) {
        Teacher teacher = teacherService.findByName(teacherName);
        if (teacher == null) {
            return null;
        }

        Set<Course> courses = teacher.getCourses();
        return getTimeTable(courses, courseDate, weekNumber, weekNumberFrom, weekNumberTo, semesterName);
    }

    @Cacheable("getAllByStudent")
    public Map<LocalDate, List<TimeTable>> getAllByStudent(String studentName, LocalDate courseDate, Integer weekNumber,
                                                           Integer weekNumberFrom, Integer weekNumberTo, String semesterName) {
        Student student = studentService.findByName(studentName);
        if (student == null) {
            return null;
        }

        Set<Course> courses = student.getCourses();
        return getTimeTable(courses, courseDate, weekNumber, weekNumberFrom, weekNumberTo, semesterName);
    }

    private Map<LocalDate, List<TimeTable>> getTimeTable(Set<Course> courses, LocalDate courseDate, Integer weekNumber,
                                                         Integer weekNumberFrom, Integer weekNumberTo, String semesterName) {
        Map<LocalDate, List<TimeTable>> map = new TreeMap<>();

        if (courseDate != null) {
            SchoolDaySwap schoolDaySwap = schoolDaySwapService.findByDate(courseDate);
            if (schoolDaySwap != null) {
                return null;
            }

            Semester semester = semesterService.findByDate(courseDate);
            if (semester != null) {
                List<TimeTable> timeTableList = timeTableRepository
                        .findTimeTableByDayOfWeekAndCourseInAndSemester(courseDate.getDayOfWeek(), courses, semester);
                if (!timeTableList.isEmpty()) {
                    map.put(courseDate, timeTableList);
                }
                return map;
            }
        }

        if (semesterName != null && weekNumber != null) {
            Semester semester = semesterService.findSemester(semesterName);
            LocalDate startOfTheWeek = semesterService.getWeekBySemesterAndWeekNumber(semesterName, weekNumber);
            if (startOfTheWeek != null) {
                getTimeTableOfTheWeek(courses, startOfTheWeek, semester, map);
            }
        }

        if (semesterName != null && weekNumberFrom != null && weekNumberTo != null) {
            Semester semester = semesterService.findSemester(semesterName);
            LocalDate startOfTheWeek = semesterService.getWeekBySemesterAndWeekNumber(semesterName, weekNumberFrom);
            if (startOfTheWeek != null) {
                int length = semester.getLength();
                weekNumberTo = weekNumberTo <= length ? weekNumberTo : length;
                for (int i = 0; i < weekNumberTo; i++) {
                    getTimeTableOfTheWeek(courses, startOfTheWeek.plusWeeks(i), semester, map);
                }
            }
        }

        return map;

    }

    private void getTimeTableOfTheWeek(Set<Course> courses, LocalDate startOfTheWeek, Semester semester, Map<LocalDate, List<TimeTable>> map) {
        LocalDate endOfTheWeek = startOfTheWeek.plusDays(DAYS_IN_WEEK - 1);
        for (int i = 0; i < DAYS_IN_WEEK - 1; i++) {
            LocalDate date = startOfTheWeek.plusDays(i);
            SchoolDaySwap schoolDaySwap = schoolDaySwapService.findByDate(date);
            if (schoolDaySwap != null) {
                LocalDate swappedDay = schoolDaySwap.getSwappedDay();
                if (swappedDay != null && (swappedDay.isBefore(endOfTheWeek) || swappedDay.isEqual(endOfTheWeek))) {
                    List<TimeTable> timeTableList = timeTableRepository
                            .findTimeTableByDayOfWeekAndCourseInAndSemester(schoolDaySwap.getSchoolDay().getDayOfWeek(), courses, semester);
                    if (!timeTableList.isEmpty()) {
                        map.put(swappedDay, timeTableList);
                    }
                }
            } else {
                List<TimeTable> timeTableList = timeTableRepository
                        .findTimeTableByDayOfWeekAndCourseInAndSemester(date.getDayOfWeek(), courses, semester);
                if (!timeTableList.isEmpty()) {
                    map.put(date, timeTableList);
                }
            }
        }
    }
}