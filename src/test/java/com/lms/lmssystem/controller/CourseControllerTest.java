package com.lms.lmssystem.controller;

import com.lms.lmssystem.dto.CourseDto;
import com.lms.lmssystem.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private CourseDto course1;
    private CourseDto course2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        course1 = new CourseDto();
        course1.setId(1L);
        course1.setName("Курс 1");

        course2 = new CourseDto();
        course2.setId(2L);
        course2.setName("Курс 2");
    }

    @Test
    void testGetAllCourses() {
        when(courseService.getAllCourse()).thenReturn(List.of(course1, course2));

        List<CourseDto> result = courseController.getAllCourses();

        assertEquals(2, result.size());
        assertEquals("Курс 1", result.get(0).getName());
        verify(courseService, times(1)).getAllCourse();
    }

    @Test
    void testGetCourseById() {
        when(courseService.getCourseById(1L)).thenReturn(course1);

        CourseDto result = courseController.getCourseById(1L);

        assertNotNull(result);
        assertEquals("Курс 1", result.getName());
        verify(courseService, times(1)).getCourseById(1L);
    }

    @Test
    void testCreateCourse() {
        when(courseService.addCourse(course1)).thenReturn(course1);

        CourseDto result = courseController.createCourse(course1);

        assertNotNull(result);
        assertEquals("Курс 1", result.getName());
        verify(courseService, times(1)).addCourse(course1);
    }

    @Test
    void testUpdateCourse() {
        when(courseService.updateCourse(course1)).thenReturn(course1);

        CourseDto result = courseController.updateCourse(course1);

        assertNotNull(result);
        assertEquals("Курс 1", result.getName());
        verify(courseService, times(1)).updateCourse(course1);
    }

    @Test
    void testDeleteCourse() {
        doNothing().when(courseService).deleteCourseById(1L);

        courseController.deleteCourse(1L);

        verify(courseService, times(1)).deleteCourseById(1L);
    }
}