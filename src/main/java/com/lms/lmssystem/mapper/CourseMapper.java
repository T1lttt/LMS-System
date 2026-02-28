package com.lms.lmssystem.mapper;

import com.lms.lmssystem.dto.ChapterDto;
import com.lms.lmssystem.dto.CourseDto;
import com.lms.lmssystem.entity.Course;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDto toDto(Course course);

    Course toModel(CourseDto courseDto);

    List<CourseDto> toDtoList(List<Course> courseList);

    List<Course> toModelList(List<CourseDto> courseDtoList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CourseDto dto, @MappingTarget Course course);
}
