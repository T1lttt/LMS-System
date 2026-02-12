package com.lms.lmssystem.mapper;

import com.lms.lmssystem.dto.CourseDto;
import com.lms.lmssystem.dto.LessonDto;
import com.lms.lmssystem.entity.Course;
import com.lms.lmssystem.entity.Lesson;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    LessonDto toDto(Lesson lesson);

    Lesson toModel(LessonDto lessonDto);

    List<LessonDto> toDtoList(List<Lesson> lessonList);

    List<Lesson> toModelList(List<LessonDto> lessonDtoList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(LessonDto dto, @MappingTarget Lesson lesson);

}
