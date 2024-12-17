package hu.cubix.university.service;

import hu.cubix.university.dto.StudentDto;
import hu.cubix.university.mapper.StudentMapper;
import hu.cubix.university.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentDto findById(Integer id) {
        return studentMapper.studentToDto(studentRepository.findById(id).orElse(null));
    }
}