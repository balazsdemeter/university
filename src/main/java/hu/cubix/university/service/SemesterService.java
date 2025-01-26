package hu.cubix.university.service;

import hu.cubix.university.enums.SemesterEnum;
import hu.cubix.university.model.Semester;
import hu.cubix.university.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SemesterService {
    private final SemesterRepository semesterRepository;

    public Semester findSemester(String semesterName) {
        List<Semester> semesters = semesterRepository.findSemesterBySemester(SemesterEnum.valueOf(semesterName));
        return semesters.stream().filter(semester -> semester.getStartDate().getYear() == LocalDate.now().getYear())
                .findAny()
                .orElse(null);
    }

    public Semester findByDate(LocalDate date) {
        List<Semester> semesters = semesterRepository.findAll();
        return semesters.stream()
                .filter(semester -> {
                    LocalDate startDate = semester.getStartDate();
                    LocalDate endDate = semester.getStartDate().plusWeeks(semester.getLength());
                    return (date.isAfter(startDate) || date.isEqual(startDate)) &&
                            (date.isBefore(endDate) || date.isEqual(endDate));
                })
                .findFirst().orElse(null);
    }

    public LocalDate getWeekBySemesterAndWeekNumber(String semesterName, int weekNumber) {
        Semester semester = findSemester(semesterName);
        if (semester != null) {
            int length = semester.getLength();
            if (weekNumber > length) {
                return null;
            }

            LocalDate startDate = semester.getStartDate();
            return startDate.plusWeeks(weekNumber);
        }
        return null;
    }
}