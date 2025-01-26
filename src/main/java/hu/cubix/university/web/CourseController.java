package hu.cubix.university.web;

import com.querydsl.core.types.Predicate;
import hu.cubix.university.api.CourseControllerApi;
import hu.cubix.university.api.model.CourseDto;
import hu.cubix.university.api.model.HistoryDataCourseDto;
import hu.cubix.university.api.model.SendMessageRequest;
import hu.cubix.university.api.model.StudentDto;
import hu.cubix.university.mapper.HistoryDataMapper;
import hu.cubix.university.model.Course;
import hu.cubix.university.model.HistoryData;
import hu.cubix.university.service.CourseService;
import hu.cubix.university.ws.ChatMessage;
import hu.cubix.university.ws.CourseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class CourseController implements CourseControllerApi {
    private final NativeWebRequest nativeWebRequest;
    private final CourseService courseService;
    private final HistoryDataMapper historyDataMapper;
    private final PageableHandlerMethodArgumentResolver pageableResolver;
    private final QuerydslPredicateArgumentResolver prediacateResolver;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

    @Override
    public ResponseEntity<CourseDto> findCourseById(Integer id) {
        CourseDto courseDto = courseService.findById(id);
        return courseDto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(courseDto);
    }

    @Override
    public ResponseEntity<List<HistoryDataCourseDto>> getHistoryById(Integer id) {
        List<HistoryDataCourseDto> courseDtos = new ArrayList<>();
        List<HistoryData<CourseDto>> courseHistory = courseService.getCourseHistory(id);
        courseHistory.forEach(dto -> courseDtos.add(historyDataMapper.courseHistoryDataToDto(dto)));
        return ResponseEntity.ok(courseDtos);
    }

    @Override
    public ResponseEntity<List<CourseDto>> searchCourses(Boolean full, Integer page, Integer size, String sort, Object body) {
        Predicate predicate = createPredicate("configurePredicate");
        Pageable pageable = createPageable("configPageable");

        return ResponseEntity.ok(courseService.search(predicate, full != null ? full : false, pageable));
    }

    public void configPageable(@SortDefault("id") Pageable pageable) {
    }

    private Pageable createPageable(String pageableConfigurerMethodName) {
        Method method;
        try {
            method = this.getClass().getMethod(pageableConfigurerMethodName, Pageable.class);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        MethodParameter methodParameter = new MethodParameter(method, 0);
        ModelAndViewContainer mavContainer = null;
        WebDataBinderFactory binderFactory = null;
        Pageable pageable = pageableResolver.resolveArgument(methodParameter, mavContainer, nativeWebRequest,
                binderFactory);
        return pageable;
    }

    public void configurePredicate(@QuerydslPredicate(root = Course.class) Predicate predicate) {}

    private Predicate createPredicate(String configMethodName) {
        Method method;
        try {
            method = this.getClass().getMethod(configMethodName, Predicate.class);
            MethodParameter methodParameter = new MethodParameter(method, 0);
            ModelAndViewContainer mavContainer = null;
            WebDataBinderFactory binderFactory = null;
            return (Predicate) prediacateResolver.resolveArgument(methodParameter, mavContainer, nativeWebRequest, binderFactory);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Void> addToTimeTable(Integer courseId, String startTime, String endTime, String dayOfWeek, String semester) {
        courseService.addToTimeTable(courseId, startTime, endTime, dayOfWeek, semester);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> cancelCourse(Integer courseId, LocalDateTime date, SendMessageRequest sendMessageRequest) {
        CourseDto courseDto = courseService.findById(courseId);
        List<StudentDto> students = courseDto.getStudents();
        students.forEach(studentDto -> {
            this.messagingTemplate.convertAndSend("/topic/timetable/" + studentDto.getId(), new CourseMessage(courseDto.getName(), date, sendMessageRequest.getMessage()));
        });
        return ResponseEntity.ok().build();
    }
}