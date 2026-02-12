package com.lms.lmssystem.service.impl;

import com.lms.lmssystem.dto.ChapterDto;
import com.lms.lmssystem.entity.Chapter;
import com.lms.lmssystem.entity.Course;
import com.lms.lmssystem.mapper.ChapterMapper;
import com.lms.lmssystem.repository.ChapterRepository;
import com.lms.lmssystem.repository.CourseRepository;
import com.lms.lmssystem.service.ChapterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private final ChapterMapper chapterMapper;

    @Transactional(readOnly = true)
    @Override
    public List<ChapterDto> getAllChapters() {
        log.info("Fetching all chapters");

        List<ChapterDto> chapters = chapterRepository.findAll()
                .stream()
                .map(chapterMapper::toDto)
                .toList();

        log.debug("Fetched {} chapters: {}", chapters.size(), chapters);

        return chapters;
    }

    @Override
    public ChapterDto getChapterById(long id) {
        log.info("Fetching chapter by id={}", id);

        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Chapter not found. chapterId={}", id);
                    return new EntityNotFoundException("Глава с id=" + id + " не найдена");
                });

        log.debug("Chapter fetched: {}", chapter);

        return chapterMapper.toDto(chapter);
    }

    @Override
    public void deleteChapterById(long id) {
        log.info("Deleting chapter with id={}", id);

        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Chapter not found. chapterId={}", id);
                    return new EntityNotFoundException("Глава с id=" + id + " не найдена");
                });

        chapterRepository.delete(chapter);

        log.info("Chapter deleted successfully. chapterId={}", id);
    }

    @Override
    public ChapterDto addChapter(ChapterDto chapterDto) {
        log.info("Creating new chapter");

        if (chapterDto.getId() != null) {
            log.error("Attempt to create chapter with predefined id: {}", chapterDto.getId());
            throw new IllegalArgumentException("ID не должен передаваться при создании");
        }

        if (chapterDto.getCourseId() == null) {
            log.error("courseId is null while creating chapter");
            throw new IllegalArgumentException("courseId обязателен");
        }

        Course course = courseRepository.findById(chapterDto.getCourseId())
                .orElseThrow(() -> {
                    log.error("Course not found. courseId={}", chapterDto.getCourseId());
                    return new EntityNotFoundException(
                            "Курс с id=" + chapterDto.getCourseId() + " не найден");
                });

        log.debug("Chapter data before save: {}", chapterDto);

        Chapter chapter = chapterMapper.toModel(chapterDto);
        chapter.setCourse(course);

        Chapter savedChapter = chapterRepository.save(chapter);

        log.info("Chapter created successfully. chapterId={}", savedChapter.getId());

        return chapterMapper.toDto(savedChapter);
    }

    @Override
    public ChapterDto updateChapter(ChapterDto chapterDto) {
        log.info("Updating chapter. chapterId={}", chapterDto.getId());

        if (chapterDto.getId() == null) {
            log.error("Chapter id is null on update");
            throw new IllegalArgumentException("Идентификатор главы не должен быть пустым.");
        }

        Chapter existing = chapterRepository.findById(chapterDto.getId())
                .orElseThrow(() -> {
                    log.error("Chapter not found. chapterId={}", chapterDto.getId());
                    return new EntityNotFoundException(
                            "Глава с id=" + chapterDto.getId() + " не найдена");
                });

        log.debug("Chapter data before update: {}", chapterDto);

        chapterMapper.updateFromDto(chapterDto, existing);

        if (chapterDto.getCourseId() != null) {
            Course course = courseRepository.findById(chapterDto.getCourseId())
                    .orElseThrow(() -> {
                        log.error("Course not found. courseId={}", chapterDto.getCourseId());
                        return new EntityNotFoundException(
                                "Курс с id=" + chapterDto.getCourseId() + " не найден");
                    });

            existing.setCourse(course);
        }

        Chapter saved = chapterRepository.save(existing);

        log.info("Chapter updated successfully. chapterId={}", saved.getId());

        return chapterMapper.toDto(saved);
    }
}

