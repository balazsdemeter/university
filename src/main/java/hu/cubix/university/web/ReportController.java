package hu.cubix.university.web;

import hu.cubix.university.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/courses/report")
public class ReportController {
    private final CourseService courseService;

    @GetMapping
    @Async
    public CompletableFuture<Integer> generateReport() {
        Integer averageSemesters = courseService.getAverageSemesters();
        return CompletableFuture.completedFuture(averageSemesters);
    }
}