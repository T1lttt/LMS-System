package com.lms.lmssystem.service.impl;

import com.lms.lmssystem.dto.LessonDto;
import com.lms.lmssystem.entity.Chapter;
import com.lms.lmssystem.entity.Lesson;
import com.lms.lmssystem.mapper.LessonMapper;
import com.lms.lmssystem.repository.ChapterRepository;
import com.lms.lmssystem.repository.LessonRepository;
import com.lms.lmssystem.service.LessonService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final ChapterRepository chapterRepository;

    @Transactional(readOnly = true)
    @Override
    public List<LessonDto> getAllLesson() {

        log.info("Fetching all lessons");

        List<LessonDto> lessons = lessonRepository.findAll()
                .stream()
                .map(lessonMapper::toDto)
                .toList();

        log.debug("Fetched {} lessons", lessons.size());

        return lessons;
    }

    @Override
    public LessonDto getLessonById(long id) {

        log.info("Fetching lesson by id={}", id);

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Lesson not found. lessonId={}", id);
                    return new EntityNotFoundException("Лекция с id =" + id + "не найдена");
                });

        log.debug("Lesson fetched: {}", lesson);

        return lessonMapper.toDto(lesson);
    }

    @Override
    public void deleteLessonById(long id) {

        log.info("Deleting lesson with id={}", id);

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Lesson not found. lessonId={}", id);
                    return new EntityNotFoundException("Лекция с id=" + id + " не найдена");
                });

        lessonRepository.delete(lesson);

        log.info("Lesson deleted successfully. lessonId={}", id);

    }

    @Override
    public LessonDto addLesson(LessonDto lessonDto) {

        log.info("Creating new lesson");

        if (lessonDto.getId() != null) {
            log.error("Attempt to create lesson with predefined id: {}", lessonDto.getId());
            throw new IllegalArgumentException("ID не должен передаваться при создании");
        }

        if (lessonDto.getChapterId() == null) {
            log.error("chapterId is null while creating lesson");
            throw new IllegalArgumentException("chapterId обязателен");
        }

        Chapter chapter = chapterRepository.findById(lessonDto.getChapterId())
                .orElseThrow(() -> {
                    log.error("Chapter not found. chapterId={}", lessonDto.getChapterId());
                    return new EntityNotFoundException(
                            "Глава с id=" + lessonDto.getChapterId() + " не найдена"
                    );
                });

        log.debug("Lesson data before save: {}", lessonDto);


        Lesson lesson = lessonMapper.toModel(lessonDto);
        lesson.setChapter(chapter);

        Lesson savedLesson = lessonRepository.save(lesson);

        log.info("Lesson created successfully. lessonId={}", savedLesson.getId());

        return lessonMapper.toDto(savedLesson);
    }

    @Override
    public LessonDto updateLesson(LessonDto lessonDto) {

        log.info("Updating lesson. lessonId={}", lessonDto.getId());

        if (lessonDto.getId() == null) {
            log.error("Lesson id is null on update");
            throw new IllegalArgumentException("Идентификатор лекции не должен быть пустым.");
        }

        Lesson existing = lessonRepository.findById(lessonDto.getId())
                .orElseThrow(() -> {
                    log.error("Lesson not found. lessonId={}", lessonDto.getId());
                    return new EntityNotFoundException(
                            "Лекция с id=" + lessonDto.getId() + " не найдена"
                    );
                });

        log.debug("Lesson data before update: {}", lessonDto);

        lessonMapper.updateFromDto(lessonDto, existing);

        Lesson saved = lessonRepository.save(existing);

        log.info("Lesson updated successfully. lessonId={}", saved.getId());

        return lessonMapper.toDto(saved);
    }
}
