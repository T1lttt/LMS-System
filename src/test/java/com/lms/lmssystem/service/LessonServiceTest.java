package com.lms.lmssystem.service;

import com.lms.lmssystem.dto.LessonDto;
import com.lms.lmssystem.entity.Chapter;
import com.lms.lmssystem.entity.Lesson;
import com.lms.lmssystem.mapper.LessonMapper;
import com.lms.lmssystem.repository.ChapterRepository;
import com.lms.lmssystem.repository.LessonRepository;
import com.lms.lmssystem.service.impl.LessonServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LessonServiceImplTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private LessonMapper lessonMapper;

    @Mock
    private ChapterRepository chapterRepository;

    @InjectMocks
    private LessonServiceImpl lessonService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllLesson_success() {
        Lesson lesson = new Lesson();
        LessonDto dto = new LessonDto();

        when(lessonRepository.findAll()).thenReturn(List.of(lesson));
        when(lessonMapper.toDto(lesson)).thenReturn(dto);

        List<LessonDto> result = lessonService.getAllLesson();

        assertEquals(1, result.size());
        verify(lessonRepository).findAll();
        verify(lessonMapper).toDto(lesson);
    }

    @Test
    void getLessonById_success() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);

        LessonDto dto = new LessonDto();
        dto.setId(1L);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(lessonMapper.toDto(lesson)).thenReturn(dto);

        LessonDto result = lessonService.getLessonById(1L);

        assertEquals(1L, result.getId());
        verify(lessonRepository).findById(1L);
        verify(lessonMapper).toDto(lesson);
    }

    @Test
    void getLessonById_notFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> lessonService.getLessonById(1L));

        verify(lessonRepository).findById(1L);
    }

    @Test
    void deleteLessonById_success() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        lessonService.deleteLessonById(1L);

        verify(lessonRepository).delete(lesson);
    }

    @Test
    void deleteLessonById_notFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> lessonService.deleteLessonById(1L));

        verify(lessonRepository, never()).delete(any());
    }

    @Test
    void addLesson_success() {
        LessonDto dto = new LessonDto();
        dto.setChapterId(5L);

        Chapter chapter = new Chapter();
        chapter.setId(5L);

        Lesson model = new Lesson();
        Lesson saved = new Lesson();
        saved.setId(10L);

        LessonDto savedDto = new LessonDto();
        savedDto.setId(10L);

        when(chapterRepository.findById(5L)).thenReturn(Optional.of(chapter));
        when(lessonMapper.toModel(dto)).thenReturn(model);
        when(lessonRepository.save(model)).thenReturn(saved);
        when(lessonMapper.toDto(saved)).thenReturn(savedDto);

        LessonDto result = lessonService.addLesson(dto);

        assertEquals(10L, result.getId());
        verify(chapterRepository).findById(5L);
        verify(lessonRepository).save(model);
    }

    @Test
    void addLesson_withId_shouldThrow() {
        LessonDto dto = new LessonDto();
        dto.setId(1L);

        assertThrows(IllegalArgumentException.class,
                () -> lessonService.addLesson(dto));

        verify(lessonRepository, never()).save(any());
    }

    @Test
    void addLesson_chapterIdNull_shouldThrow() {
        LessonDto dto = new LessonDto();

        assertThrows(IllegalArgumentException.class,
                () -> lessonService.addLesson(dto));

        verify(chapterRepository, never()).findById(any());
    }

    @Test
    void addLesson_chapterNotFound_shouldThrow() {
        LessonDto dto = new LessonDto();
        dto.setChapterId(5L);

        when(chapterRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> lessonService.addLesson(dto));

        verify(lessonRepository, never()).save(any());
    }

    @Test
    void updateLesson_success() {
        LessonDto dto = new LessonDto();
        dto.setId(1L);

        Lesson existing = new Lesson();
        existing.setId(1L);

        Lesson saved = new Lesson();
        saved.setId(1L);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(lessonRepository.save(existing)).thenReturn(saved);
        when(lessonMapper.toDto(saved)).thenReturn(dto);

        LessonDto result = lessonService.updateLesson(dto);

        assertEquals(1L, result.getId());
        verify(lessonMapper).updateFromDto(dto, existing);
        verify(lessonRepository).save(existing);
    }

    @Test
    void updateLesson_idNull_shouldThrow() {
        LessonDto dto = new LessonDto();

        assertThrows(IllegalArgumentException.class,
                () -> lessonService.updateLesson(dto));

        verify(lessonRepository, never()).save(any());
    }

    @Test
    void updateLesson_notFound_shouldThrow() {
        LessonDto dto = new LessonDto();
        dto.setId(1L);

        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> lessonService.updateLesson(dto));

        verify(lessonRepository).findById(1L);
    }
}