package hu.cubix.university.web;

import hu.cubix.university.dto.StudentDto;
import hu.cubix.university.service.StudentService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> findById(@PathVariable @NotNull Integer id) {
        StudentDto studentDto = studentService.findById(id);
        return studentDto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(studentDto);
    }
}
