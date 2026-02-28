package com.lms.lmssystem.mapper;


import com.lms.lmssystem.dto.LessonDto;

import com.lms.lmssystem.entity.Lesson;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;



@Mapper(componentModel = "spring")
public interface LessonMapper {

    LessonDto toDto(Lesson lesson);

    Lesson toModel(LessonDto lessonDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(LessonDto dto, @MappingTarget Lesson lesson);

}
