package hu.cubix.university.repository;

import hu.cubix.university.model.SchoolDaySwap;
import nonapi.io.github.classgraph.utils.VersionFinder.OperatingSystem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SchoolDaySwapRepository extends JpaRepository<SchoolDaySwap, Integer>  {
    List<SchoolDaySwap> findBySchoolDayIsGreaterThanEqual(LocalDate date);
    Optional<SchoolDaySwap> findSchoolDaySwapBySchoolDay(LocalDate date);
}