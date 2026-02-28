package com.lms.lmssystem.service;

import com.lms.lmssystem.dto.CourseDto;
import com.lms.lmssystem.entity.Course;
import com.lms.lmssystem.mapper.CourseMapper;
import com.lms.lmssystem.repository.CourseRepository;
import com.lms.lmssystem.service.impl.CourseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCourse_success() {
        Course course = new Course();
        CourseDto dto = new CourseDto();

        when(courseRepository.findAll()).thenReturn(List.of(course));
        when(courseMapper.toDto(course)).thenReturn(dto);

        List<CourseDto> result = courseService.getAllCourse();

        assertEquals(1, result.size());
        verify(courseRepository).findAll();
        verify(courseMapper).toDto(course);
    }

    @Test
    void getCourseById_success() {
        Course course = new Course();
        course.setId(1L);

        CourseDto dto = new CourseDto();
        dto.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseMapper.toDto(course)).thenReturn(dto);

        CourseDto result = courseService.getCourseById(1L);

        assertEquals(1L, result.getId());
        verify(courseRepository).findById(1L);
        verify(courseMapper).toDto(course);
    }

    @Test
    void getCourseById_notFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> courseService.getCourseById(1L));

        verify(courseRepository).findById(1L);
    }

    @Test
    void deleteCourseById_success() {
        Course course = new Course();
        course.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.deleteCourseById(1L);

        verify(courseRepository).delete(course);
    }

    @Test
    void deleteCourseById_notFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> courseService.deleteCourseById(1L));

        verify(courseRepository).findById(1L);
        verify(courseRepository, never()).delete(any());
    }

    @Test
    void addCourse_success() {
        CourseDto dto = new CourseDto();

        Course model = new Course();
        Course saved = new Course();
        saved.setId(10L);

        CourseDto savedDto = new CourseDto();
        savedDto.setId(10L);

        when(courseMapper.toModel(dto)).thenReturn(model);
        when(courseRepository.save(model)).thenReturn(saved);
        when(courseMapper.toDto(saved)).thenReturn(savedDto);

        CourseDto result = courseService.addCourse(dto);

        assertEquals(10L, result.getId());
        verify(courseRepository).save(model);
    }

    @Test
    void addCourse_withId_shouldThrow() {
        CourseDto dto = new CourseDto();
        dto.setId(1L);

        assertThrows(IllegalArgumentException.class,
                () -> courseService.addCourse(dto));

        verify(courseRepository, never()).save(any());
    }

    @Test
    void updateCourse_success() {
        CourseDto dto = new CourseDto();
        dto.setId(1L);

        Course existing = new Course();
        existing.setId(1L);

        Course saved = new Course();
        saved.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(existing)).thenReturn(saved);
        when(courseMapper.toDto(saved)).thenReturn(dto);

        CourseDto result = courseService.updateCourse(dto);

        assertEquals(1L, result.getId());
        verify(courseMapper).updateFromDto(dto, existing);
        verify(courseRepository).save(existing);
    }

    @Test
    void updateCourse_idNull_shouldThrow() {
        CourseDto dto = new CourseDto();

        assertThrows(IllegalArgumentException.class,
                () -> courseService.updateCourse(dto));

        verify(courseRepository, never()).save(any());
    }

    @Test
    void updateCourse_notFound_shouldThrow() {
        CourseDto dto = new CourseDto();
        dto.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> courseService.updateCourse(dto));

        verify(courseRepository).findById(1L);
    }
}