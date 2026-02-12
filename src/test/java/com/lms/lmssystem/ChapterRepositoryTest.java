package com.lms.lmssystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.lms.lmssystem.entity.Chapter;
import com.lms.lmssystem.entity.Course;
import com.lms.lmssystem.repository.ChapterRepository;
import com.lms.lmssystem.repository.CourseRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChapterRepositoryTest {

    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Test
    @Transactional
    void testSaveAndFindChapter() {
        Course course = new Course();
        course.setName("Курс");
        course = courseRepository.save(course);

        Chapter chapter = new Chapter();
        chapter.setName("Репозиторий");
        chapter.setDescription("Основы репозитория");
        chapter.setChapterOrder(1);
        chapter.setCourse(course);

        chapter = chapterRepository.save(chapter);

        Chapter fromDataBase = chapterRepository.findById(chapter.getId()).orElseThrow();
        assertEquals("Репозиторий", fromDataBase.getName());
        assertEquals(course.getName(),fromDataBase.getCourse().getName());

    }

}
