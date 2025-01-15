package hu.cubix.university.mapper;

import hu.cubix.university.api.model.CourseDto;
import hu.cubix.university.api.model.HistoryDataCourseDto;
import hu.cubix.university.api.model.HistoryDataStudentDto;
import hu.cubix.university.api.model.StudentDto;
import hu.cubix.university.model.HistoryData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistoryDataMapper {
	HistoryDataCourseDto courseHistoryDataToDto(HistoryData<CourseDto> dto);
	HistoryDataStudentDto studentHistoryDataToDto(HistoryData<StudentDto> dto);
}
