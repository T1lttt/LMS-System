package com.lms.lmssystem.controller;

import com.lms.lmssystem.dto.ChapterDto;
import com.lms.lmssystem.service.ChapterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChapterControllerTest {

    @Mock
    private ChapterService chapterService;

    @InjectMocks
    private ChapterController chapterController;

    private ChapterDto chapterDto1;
    private ChapterDto chapterDto2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        chapterDto1 = new ChapterDto();
        chapterDto1.setId(1L);
        chapterDto1.setName("Глава 1");
        chapterDto1.setDescription("Описание 1");
        chapterDto1.setChapterOrder(1);
        chapterDto1.setCourseId(1L);

        chapterDto2 = new ChapterDto();
        chapterDto2.setId(2L);
        chapterDto2.setName("Глава 2");
        chapterDto2.setDescription("Описание 2");
        chapterDto2.setChapterOrder(2);
        chapterDto2.setCourseId(1L);
    }

    @Test
    void testGetAllChapters() {
        when(chapterService.getAllChapters()).thenReturn(List.of(chapterDto1, chapterDto2));

        List<ChapterDto> result = chapterController.getAllChapters();

        assertEquals(2, result.size());
        assertEquals("Глава 1", result.get(0).getName());
        verify(chapterService, times(1)).getAllChapters();
    }

    @Test
    void testGetChapterById() {
        when(chapterService.getChapterById(1L)).thenReturn(chapterDto1);

        ChapterDto result = chapterController.getChapterById(1L);

        assertNotNull(result);
        assertEquals("Глава 1", result.getName());
        verify(chapterService, times(1)).getChapterById(1L);
    }

    @Test
    void testCreateChapter() {
        when(chapterService.addChapter(chapterDto1)).thenReturn(chapterDto1);

        ChapterDto result = chapterController.createChapter(chapterDto1);

        assertNotNull(result);
        assertEquals("Глава 1", result.getName());
        verify(chapterService, times(1)).addChapter(chapterDto1);
    }

    @Test
    void testUpdateChapter() {
        when(chapterService.updateChapter(chapterDto1)).thenReturn(chapterDto1);

        ChapterDto result = chapterController.updateChapter(chapterDto1);

        assertNotNull(result);
        assertEquals("Глава 1", result.getName());
        verify(chapterService, times(1)).updateChapter(chapterDto1);
    }

    @Test
    void testDeleteChapter() {
        doNothing().when(chapterService).deleteChapterById(1L);

        chapterController.deleteChapter(1L);

        verify(chapterService, times(1)).deleteChapterById(1L);
    }
}