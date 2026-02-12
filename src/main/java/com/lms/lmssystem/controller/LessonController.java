package com.lms.lmssystem.controller;

import com.lms.lmssystem.dto.LessonDto;
import com.lms.lmssystem.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Lessons", description = "Работа с лекциями")
@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @Operation(summary = "Получить все лекции")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список лекций получен")
    })
    @GetMapping
    public List<LessonDto> getAllLessons() {
        return lessonService.getAllLesson();
    }

    @Operation(summary = "Получить лекцию по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Лекция найдена"),
            @ApiResponse(responseCode = "404", description = "Лекция не найдена")
    })
    @GetMapping("/{id}")
    public LessonDto getLessonById(@PathVariable long id) {
        return lessonService.getLessonById(id);
    }

    @Operation(summary = "Создать новую лекцию")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Лекция создана"),
            @ApiResponse(responseCode = "404", description = "Глава не найдена")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public LessonDto createLesson(@RequestBody LessonDto lessonDto) {
        return lessonService.addLesson(lessonDto);
    }

    @Operation(summary = "Обновить лекцию")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Лекция обновлена"),
            @ApiResponse(responseCode = "404", description = "Лекция не найдена")
    })
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public LessonDto updateLesson(@RequestBody LessonDto lessonDto) {
        return lessonService.updateLesson(lessonDto);
    }

    @Operation(summary = "Удалить лекцию")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Лекция удалена"),
            @ApiResponse(responseCode = "404", description = "Лекция не найдена")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteLesson(@PathVariable long id) {
        lessonService.deleteLessonById(id);
    }
}
