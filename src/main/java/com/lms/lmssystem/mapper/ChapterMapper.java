package com.lms.lmssystem.mapper;

import com.lms.lmssystem.dto.ChapterDto;
import com.lms.lmssystem.entity.Chapter;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

    ChapterDto toDto(Chapter chapter);

    Chapter toModel(ChapterDto chapterDto);

    List<ChapterDto> toDtoList(List<Chapter> chapterList);

    List<Chapter> toModelList(List<ChapterDto> chapterDtoList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(ChapterDto dto, @MappingTarget Chapter chapter);

}
