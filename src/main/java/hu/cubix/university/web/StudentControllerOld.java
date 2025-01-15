package hu.cubix.university.web;

import hu.cubix.university.api.model.StudentDto;
import hu.cubix.university.model.HistoryData;
import hu.cubix.university.service.StudentService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
//@RestController
@RequestMapping("/api/students")
public class StudentControllerOld {

    private final StudentService studentService;

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> findById(@PathVariable @NotNull Integer id) {
        StudentDto studentDto = studentService.findById(id);
        return studentDto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(studentDto);
    }

    @GetMapping("/history")
    public List<HistoryData<StudentDto>> getHistory(@RequestParam("dateTime") @NotNull LocalDateTime dateTime) {
        return studentService.getHistory(dateTime);
    }
}
