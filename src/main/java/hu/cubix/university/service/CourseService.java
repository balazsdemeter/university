package hu.cubix.university.service;

import com.querydsl.core.types.Predicate;
import hu.cubix.university.api.model.CourseDto;
import hu.cubix.university.mapper.CourseMapper;
import hu.cubix.university.model.Course;
import hu.cubix.university.model.HistoryData;
import hu.cubix.university.repository.CourseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Cacheable("pagedSearchWithRelationships")
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

    @Transactional
    @SuppressWarnings({"unchecked"})
    public List<HistoryData<CourseDto>> getCourseHistory(int id) {
        List<HistoryData<Course>> courseList = AuditReaderFactory.get(entityManager)
                .createQuery()
                .forRevisionsOfEntity(Course.class, false, true)
                .add(AuditEntity.property("id").eq(id))
                .getResultList()
                .stream()
                .map(o -> {
                    Object[] objArray = (Object[]) o;
                    DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) objArray[1];
                    Course course = (Course) objArray[0];
                    course.getStudents();
                    course.getTeachers();

                    return new HistoryData<>(
                            course,
                            (RevisionType) objArray[2],
                            revisionEntity.getId(),
                            revisionEntity.getRevisionDate()
                    );
                })
                .toList();

        List<HistoryData<CourseDto>> courseDtosWithHistory = new ArrayList<>();

        courseList.forEach(hd -> courseDtosWithHistory.add(
                new HistoryData<>(
                        courseMapper.courseToDto(hd.getData()),
                        hd.getRevType(),
                        hd.getRevision(),
                        hd.getDate()
                )
        ));

        return courseDtosWithHistory;
    }
}