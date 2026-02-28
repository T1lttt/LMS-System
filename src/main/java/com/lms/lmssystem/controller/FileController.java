package com.lms.lmssystem.controller;

import com.lms.lmssystem.service.impl.FileServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Files", description = "Загрузка и скачивание файлов")
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileServiceImpl fileService;

    @Operation(summary = "Загрузить файл")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Файл успешно загружен"),
            @ApiResponse(responseCode = "403", description = "Нет прав для загрузки файла"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public String upload(@RequestParam(name = "file") MultipartFile multipartFile) {
        return fileService.uploadFile(multipartFile);
    }

    @Operation(summary = "Скачать файл по имени")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Файл успешно скачан"),
            @ApiResponse(responseCode = "403", description = "Нет прав для скачивания файла"),
            @ApiResponse(responseCode = "404", description = "Файл не найден")
    })
    @GetMapping("/download/{file}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "file") String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(fileService.downloadFile(fileName));
    }
}