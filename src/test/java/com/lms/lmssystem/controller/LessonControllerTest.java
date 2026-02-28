package com.lms.lmssystem.controller;

import com.lms.lmssystem.dto.LessonDto;
import com.lms.lmssystem.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LessonControllerTest {

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController lessonController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLessons() {
        List<LessonDto> lessons = new ArrayList<>();
        lessons.add(new LessonDto());
        when(lessonService.getAllLesson()).thenReturn(lessons);

        List<LessonDto> result = lessonController.getAllLessons();

        assertEquals(1, result.size());
        verify(lessonService, times(1)).getAllLesson();
    }

    @Test
    void testGetLessonById() {
        LessonDto lesson = new LessonDto();
        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        LessonDto result = lessonController.getLessonById(1L);

        assertNotNull(result);
        verify(lessonService, times(1)).getLessonById(1L);
    }

    @Test
    void testCreateLesson() {
        LessonDto lesson = new LessonDto();
        when(lessonService.addLesson(lesson)).thenReturn(lesson);

        LessonDto result = lessonController.createLesson(lesson);

        assertEquals(lesson, result);
        verify(lessonService, times(1)).addLesson(lesson);
    }

    @Test
    void testUpdateLesson() {
        LessonDto lesson = new LessonDto();
        when(lessonService.updateLesson(lesson)).thenReturn(lesson);

        LessonDto result = lessonController.updateLesson(lesson);

        assertEquals(lesson, result);
        verify(lessonService, times(1)).updateLesson(lesson);
    }

    @Test
    void testDeleteLesson() {
        doNothing().when(lessonService).deleteLessonById(1L);

        lessonController.deleteLesson(1L);

        verify(lessonService, times(1)).deleteLessonById(1L);
    }
}