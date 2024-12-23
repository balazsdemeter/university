package hu.cubix.university.service;

import com.querydsl.core.types.Predicate;
import hu.cubix.university.dto.CourseDto;
import hu.cubix.university.mapper.CourseMapper;
import hu.cubix.university.model.Course;
import hu.cubix.university.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public List<CourseDto> search(Predicate predicate, boolean full, Pageable pageable) {
        List<Course> courses = courseRepository.findAll(predicate, pageable).getContent();
        if (!full) {
            return courseMapper.courseSummaryListToDtos(courses);
        }

        List<Integer> ids = courses.stream().map(Course::getId).toList();
        courses = courseRepository.findAllWithStudentsById(ids);
        courses = courseRepository.findAllWithTeachersById(ids, pageable.getSort());
        return courseMapper.courseListToDtos(courses);
    }

    public CourseDto findById(Integer id) {
        return courseMapper.courseToDto(courseRepository.findById(id).orElse(null));
    }
}