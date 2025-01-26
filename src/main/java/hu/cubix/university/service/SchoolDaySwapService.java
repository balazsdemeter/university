package hu.cubix.university.service;

import hu.cubix.university.model.SchoolDaySwap;
import hu.cubix.university.repository.SchoolDaySwapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SchoolDaySwapService {
    private final SchoolDaySwapRepository repository;

    @Transactional
    public void registerDayOff(LocalDate dayOffDate, LocalDate swappedDate) {
        SchoolDaySwap schoolDaySwap = new SchoolDaySwap();
        schoolDaySwap.setSchoolDay(dayOffDate);
        schoolDaySwap.setSwappedDay(swappedDate);
        repository.save(schoolDaySwap);
    }

    public SchoolDaySwap findByDate(LocalDate date) {
        return repository.findSchoolDaySwapBySchoolDay(date).orElse(null);
    }
}