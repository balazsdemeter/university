package hu.cubix.university.web;

import hu.cubix.university.api.StudentControllerApi;
import hu.cubix.university.api.model.HistoryDataStudentDto;
import hu.cubix.university.api.model.StudentDto;
import hu.cubix.university.mapper.HistoryDataMapper;
import hu.cubix.university.model.HistoryData;
import hu.cubix.university.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class StudentController implements StudentControllerApi {
    private final NativeWebRequest nativeWebRequest;
    private final StudentService studentService;
    private final HistoryDataMapper historyDataMapper;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

    @Override
    public ResponseEntity<StudentDto> findStudentById(Integer id) {
        StudentDto studentDto = studentService.findById(id);
        return studentDto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(studentDto);
    }

    @Override
    public ResponseEntity<List<HistoryDataStudentDto>> getHistory(LocalDateTime dateTime) {
        List<HistoryDataStudentDto> studentDtos = new ArrayList<>();
        List<HistoryData<StudentDto>> history = studentService.getHistory(dateTime);
        history.forEach(studentDtoHistoryData -> studentDtos.add(historyDataMapper.studentHistoryDataToDto(studentDtoHistoryData)));
        return ResponseEntity.ok(studentDtos);
    }
}
