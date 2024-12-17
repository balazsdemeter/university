package hu.cubix.university.web;

import hu.cubix.university.dto.StudentDto;
import hu.cubix.university.dto.TeacherDto;
import hu.cubix.university.service.TeacherService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService findById;

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> findById(@PathVariable @NotNull Integer id) {
        TeacherDto teacherDto = findById.findById(id);
        return teacherDto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(teacherDto);
    }
}