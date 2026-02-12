package com.lms.lmssystem.service;

import com.lms.lmssystem.dto.CourseDto;
import com.lms.lmssystem.dto.LessonDto;

import java.util.List;

public interface LessonService {

    List<LessonDto> getAllLesson();

    LessonDto getLessonById(long id);

    void deleteLessonById(long id);

    LessonDto addLesson(LessonDto lessonDto);

    LessonDto updateLesson(LessonDto lessonDto);

}
