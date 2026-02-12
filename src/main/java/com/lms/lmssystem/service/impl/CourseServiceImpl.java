package com.lms.lmssystem.service.impl;

import com.lms.lmssystem.dto.ChapterDto;
import com.lms.lmssystem.dto.CourseDto;
import com.lms.lmssystem.entity.Course;
import com.lms.lmssystem.mapper.CourseMapper;
import com.lms.lmssystem.repository.CourseRepository;
import com.lms.lmssystem.service.ChapterService;
import com.lms.lmssystem.service.CourseService;
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
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CourseDto> getAllCourse() {
        log.info("Fetching all courses");

        List<CourseDto> courses = courseRepository.findAll()
                .stream()
                .map(courseMapper::toDto)
                .toList();

        log.debug("Fetched {} courses: {}", courses.size(), courses);

        return courses;
    }

    @Override
    public CourseDto getCourseById(long id) {
        log.info("Fetching course by id={}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Course not found. courseId={}", id);
                    return new EntityNotFoundException("Курс с id=" + id + " не найден");
                });

        log.debug("Course fetched: {}", course);

        return courseMapper.toDto(course);
    }

    @Override
    public void deleteCourseById(long id) {
        log.info("Deleting course with id={}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Course not found. courseId={}", id);
                    return new EntityNotFoundException("Курс с id=" + id + " не найден");
                });

        courseRepository.delete(course);

        log.info("Course deleted successfully. courseId={}", id);
    }

    @Override
    public CourseDto addCourse(CourseDto courseDto) {
        log.info("Creating new course");

        if (courseDto.getId() != null) {
            log.error("Attempt to create course with predefined id: {}", courseDto.getId());
            throw new IllegalArgumentException("ID не должен передаваться при создании");
        }

        log.debug("Course data before save: {}", courseDto);

        Course course = courseMapper.toModel(courseDto);
        Course savedCourse = courseRepository.save(course);

        log.info("Course created successfully. courseId={}", savedCourse.getId());

        return courseMapper.toDto(savedCourse);
    }

    @Override
    public CourseDto updateCourse(CourseDto courseDto) {
        log.info("Updating course. courseId={}", courseDto.getId());

        if (courseDto.getId() == null) {
            log.error("Course id is null on update");
            throw new IllegalArgumentException("Идентификатор курса не должен быть пустым.");
        }

        Course existing = courseRepository.findById(courseDto.getId())
                .orElseThrow(() -> {
                    log.error("Course not found. courseId={}", courseDto.getId());
                    return new EntityNotFoundException(
                            "Курс с id=" + courseDto.getId() + " не найден"
                    );
                });

        log.debug("Course data before update: {}", courseDto);

        courseMapper.updateFromDto(courseDto, existing);

        Course saved = courseRepository.save(existing);

        log.info("Course updated successfully. courseId={}", saved.getId());

        return courseMapper.toDto(saved);
    }
}
