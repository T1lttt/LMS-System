package com.lms.lmssystem.service;

import com.lms.lmssystem.dto.ChapterDto;

import java.util.List;

public interface ChapterService {

    List<ChapterDto> getAllChapters();

    ChapterDto getChapterById(long id);

    void deleteChapterById(long id);

    ChapterDto addChapter(ChapterDto chapterDto);

    ChapterDto updateChapter(ChapterDto chapterDto);
}
