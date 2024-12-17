package hu.cubix.university.service;

import com.querydsl.core.types.Predicate;
import hu.cubix.university.dto.CourseDto;
import hu.cubix.university.mapper.CourseMapper;
import hu.cubix.university.model.Course;
import hu.cubix.university.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public List<CourseDto> search(Predicate predicate, boolean full, Pageable pageable) {
        Iterable<Course> courses = courseRepository.findAll(predicate, pageable.getSort());
        List<CourseDto> courseDtos = new ArrayList<>();
        courses.forEach(course ->
            courseDtos.add(full ? courseMapper.courseToDto(course) : courseMapper.courseSummaryToDto(course))
        );
        return courseDtos;
    }

    public CourseDto findById(Integer id) {
        return courseMapper.courseToDto(courseRepository.findById(id).orElse(null));
    }
}