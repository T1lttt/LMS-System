package com.lms.lmssystem.controller;

import com.lms.lmssystem.dto.ChapterDto;
import com.lms.lmssystem.service.ChapterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chapters", description = "Работа с главами")
@RestController
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    @Operation(summary = "Получить все главы")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список глав получен")
    })
    @GetMapping
    public List<ChapterDto> getAllChapters() {
        return chapterService.getAllChapters();
    }

    @Operation(summary = "Получить главу по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Глава найдена"),
            @ApiResponse(responseCode = "404", description = "Глава не найдена")
    })
    @GetMapping("/{id}")
    public ChapterDto getChapterById(@PathVariable long id) {
        return chapterService.getChapterById(id);
    }

    @Operation(summary = "Создать новую главу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Глава создана"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ChapterDto createChapter(@RequestBody ChapterDto chapterDto) {
        return chapterService.addChapter(chapterDto);
    }

    @Operation(summary = "Обновить главу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Глава обновлена"),
            @ApiResponse(responseCode = "404", description = "Глава или курс не найдены")
    })
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ChapterDto updateChapter(@RequestBody ChapterDto chapterDto) {
        return chapterService.updateChapter(chapterDto);
    }

    @Operation(summary = "Удалить главу")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Глава удалена"),
            @ApiResponse(responseCode = "404", description = "Глава не найдена")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteChapter(@PathVariable long id) {
        chapterService.deleteChapterById(id);
    }
}
