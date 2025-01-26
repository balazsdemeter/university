package hu.cubix.university.service;

import hu.cubix.university.api.model.StudentDto;
import hu.cubix.university.aspect.Retryable;
import hu.cubix.university.mapper.StudentMapper;
import hu.cubix.university.model.HistoryData;
import hu.cubix.university.model.Image;
import hu.cubix.university.model.Student;
import hu.cubix.university.repository.ImageRepository;
import hu.cubix.university.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

@RequiredArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final MockService mockService;
    private final ImageRepository imageRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public StudentDto findById(Integer id) {
        return studentMapper.studentToDto(studentRepository.findById(id).orElse(null));
    }

    @Scheduled(cron = "${student.sync.timer}")
    @SchedulerLock(name = "syncStudents")
    @Async
    @Retryable
    public void syncStudents() throws TimeoutException {
        List<Student> students = studentRepository.findAll();
        for (Student student : students) {
            int numberOfFreeSemesters = mockService.getNumberOfFreeSemesters(student.getExternalId());
            student.setNumberOfFreeSemesters(numberOfFreeSemesters);
            studentRepository.save(student);
        }
    }

    @Transactional
    @SuppressWarnings({"unchecked"})
    public List<HistoryData<StudentDto>> getHistory(LocalDateTime dateTime) {
        List<HistoryData<Student>> studentList = AuditReaderFactory.get(entityManager)
                .createQuery()
                .forRevisionsOfEntity(Student.class, false, true)
                .getResultList()
                .stream()
                .map(o -> {
                    Object[] objArray = (Object[]) o;
                    DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) objArray[1];
                    Student student = (Student) objArray[0];

                    return new HistoryData<>(
                            student,
                            (RevisionType) objArray[2],
                            revisionEntity.getId(),
                            revisionEntity.getRevisionDate()
                    );
                })
                .toList();

        Date date = java.util.Date
                .from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        studentList = studentList.stream().filter(studentHistoryData -> studentHistoryData.getDate().before(date)).toList();

        List<HistoryData<StudentDto>> studentDtosWithHistory = new ArrayList<>();

        studentList.forEach(hd -> studentDtosWithHistory.add(
                new HistoryData<>(
                        studentMapper.studentToDto(hd.getData()),
                        hd.getRevType(),
                        hd.getRevision(),
                        hd.getDate()
                )
        ));

        return studentDtosWithHistory;
    }

    @Transactional
    public Long createOrUpdateImage(int id, byte[] bytes) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return null;
        }

        student.getImages().clear();

        Image image = Image.builder()
                .data(bytes)
                .build();
        image = imageRepository.save(image);
        student.getImages().add(image);
        return image.getId();
    }

    @Transactional
    public Long findImage(int id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return null;
        }

        Image image = student.getImages().stream().findFirst().orElse(null);

        return image != null ? image.getId() : null;
    }

    @Transactional
    public void deleteImage(int id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            Set<Image> images = student.getImages();
            if (images != null) {
                images.clear();
            }
            studentRepository.save(student);
        }
    }

    public Student findByName(String name) {
        return studentRepository.findStudentByName(name).orElse(null);
    }
}