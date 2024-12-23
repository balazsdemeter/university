package hu.cubix.university.web;

import com.querydsl.core.types.Predicate;
import hu.cubix.university.dto.CourseDto;
import hu.cubix.university.model.Course;
import hu.cubix.university.service.CourseService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/search")
    public List<CourseDto> searchCourses(@QuerydslPredicate(root = Course.class) Predicate predicate,
                                         @RequestParam Optional<Boolean> full, @SortDefault("id") Pageable pageable) {
        return courseService.search(predicate, full.orElse(false), pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> findById(@PathVariable @NotNull Integer id) {
        CourseDto courseDto = courseService.findById(id);
        return courseDto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(courseDto);
    }
}