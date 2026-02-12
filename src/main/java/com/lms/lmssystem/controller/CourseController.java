package com.lms.lmssystem.controller;

import com.lms.lmssystem.dto.CourseDto;
import com.lms.lmssystem.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Courses", description = "Работа с курсами")
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Получить все курсы")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список курсов получен")
    })
    @GetMapping
    public List<CourseDto> getAllCourses() {
        return courseService.getAllCourse();
    }

    @Operation(summary = "Получить курс по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс найден"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @GetMapping("/{id}")
    public CourseDto getCourseById(@PathVariable long id) {
        return courseService.getCourseById(id);
    }

    @Operation(summary = "Создать новый курс")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс создан")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CourseDto createCourse(@RequestBody CourseDto courseDto) {
        return courseService.addCourse(courseDto);
    }

    @Operation(summary = "Обновить курс")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс обновлён"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CourseDto updateCourse(@RequestBody CourseDto courseDto) {
        return courseService.updateCourse(courseDto);
    }

    @Operation(summary = "Удалить курс")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Курс удалён"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCourse(@PathVariable long id) {
        courseService.deleteCourseById(id);
    }
}

