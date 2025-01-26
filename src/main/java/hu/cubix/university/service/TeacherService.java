package hu.cubix.university.service;

import hu.cubix.university.api.model.TeacherDto;
import hu.cubix.university.mapper.TeacherMapper;
import hu.cubix.university.model.Teacher;
import hu.cubix.university.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    public TeacherDto findById(Integer id) {
        Teacher teacher = teacherRepository.findById(id).orElse(null);
        return teacherMapper.teacherToDto(teacher);
    }

    public Teacher findByName(String name) {
        return teacherRepository.findTeacherByName(name).orElse(null);
    }
}
