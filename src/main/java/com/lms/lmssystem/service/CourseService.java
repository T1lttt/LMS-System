package com.lms.lmssystem.service;

import com.lms.lmssystem.dto.ChapterDto;
import com.lms.lmssystem.dto.CourseDto;

import java.util.List;

public interface CourseService {

    List<CourseDto> getAllCourse();

    CourseDto getCourseById(long id);

    void deleteCourseById(long id);

    CourseDto addCourse(CourseDto courseDto);

    CourseDto updateCourse(CourseDto courseDto);

}
